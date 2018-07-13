package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject


class ClarifyingQuestions : UiComponent<Command, Result, ViewState> {

    val cmd = PublishSubject.create<Command>()

    fun fromEvent(command: Command) {
        cmd.onNext(command)
    }

    lateinit var results: Observable<Result>

    override fun process(commands: Observable<Command>): Observable<Result> {
        val cast = commands
                .mergeWith(cmd.doOnNext { println("CL MERGEd CMD") })
                .doOnNext { println("CMDCQ " + it) }
                .compose(paProcessor)
                .doOnNext { println("RESCQ " + it) }
                .cast(Result::class.java)
                .publish { shared ->
                    Observable.merge(
                            shared,
                            shared.doOnNext { println("input222") }.compose(paAnsweredProcessor)//.skip(1)
                    ).doOnNext { println("COMB $it") }
                }
                .replay()
                .refCount()

        results = cast

        return cast
    }

    override fun render(): Observable<ViewState> {
        return results.compose<ViewState>(clarifyingQuestionsReducer)
    }

    data class AllQuestionsAnswered(
            val totalQuestions: Int? = null,
            val answeredQuestions: MutableSet<String> = mutableSetOf("1234")
    )


    val paAnsweredProcessor =
            ObservableTransformer<Result, Result> { t ->
                t.doOnNext { println("111 $it") }
                        .scan(AllQuestionsAnswered()) { state, result ->
                            when (result) {
                                is Result.QuestionsLoaded -> {
                                    state.copy(result.questions.size, mutableSetOf())
                                }
                                is Result.NoQuestionsRequired -> {
                                    AllQuestionsAnswered()
                                    //state.copy(0, mutableSetOf())
                                }
                                is Result.ValidAnswer -> {
                                    state.copy(answeredQuestions = state.answeredQuestions.apply { add(result.id) })
                                }
                                is Result.EmptyAnswer -> {
                                    state.copy(answeredQuestions = state.answeredQuestions.apply { remove(result.id) })
                                }
                                else -> throw IllegalStateException("sdf")
                            }
                        }
                        .skip(1)
                        .flatMap {
                            //implicitly handles "no questions" case
                            if (it.totalQuestions == null) {
                                println("value! $it")
                                Observable.empty<Result>()
                            } else {
                                Observable.just(Result.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size))
                            }
                        }

            }


    val paProcessor =
            ObservableTransformer<UiCommand, UiResult> { t ->
                t.flatMap {
                    when (it) {
                        is Command.INIT -> {
                            val questions = it.itemOpportunity.itemDetails.questions
                            val answers = it.itemOpportunity.proposal.questionAnswers

                            when {
                                questions != null && answers != null -> {
                                    Observable
                                            .just(Result.QuestionsLoaded(questions))
                                            .cast(UiResult::class.java)
                                            .mergeWith(
                                                    Observable
                                                            .fromIterable(answers)
                                                            .map { Result.ValidAnswer(it.id, it.answer) }
                                            )
                                    //Observable.fromArray(Result.Questions(questions))
                                }
                                questions != null -> Observable.just(Result.QuestionsLoaded(questions))
                                else -> Observable.just(Result.NoQuestionsRequired)
                            }
                        }
                        is Command.UpdateAnswer -> {
                            val validated = it.answer.trim()


                            if (validated.isNotEmpty()) {
                                Observable.just(Result.ValidAnswer(it.id, validated))
                            } else {
                                Observable.just(Result.EmptyAnswer(it.id))
                            }
                        }
                        else -> throw IllegalStateException("sdf")
                    }
                }
            }

    sealed class Result : UiResult {
        data class QuestionsLoaded(val questions: List<Question>) : Result()
        object NoQuestionsRequired : Result()

        data class ValidAnswer(val id: String, val answer: String) : Result()
        data class EmptyAnswer(val id: String) : Result()

        data class AllQuestionsAnswered(val answered: Boolean) : Result()
    }

    sealed class Command : UiCommand {
        data class INIT(val itemOpportunity: ItemOpportunity) : Command()

        //only for internal use? from event
        data class UpdateAnswer(val id: String, val answer: String) : Command()
    }

    val clarifyingQuestionsReducer =
            ObservableTransformer<Result, ViewState> {
                it.scan(ViewState()) { state, result ->
                    when(result) {
                        is Result.QuestionsLoaded ->
                                ViewState(
                                        result.questions.map {
                                            QuestionViewState(it.id, it.question, null)
                                        }
                                )
                        is Result.ValidAnswer ->
                                ViewState(
                                        //state.items.find { it.id == result.id }.answer!! = result.answer
                                        state.items.map {
                                            if (it.id == result.id) {
                                                QuestionViewState(it.id, it.question, result.answer)
                                            } else {
                                                it
                                            }
                                        }
                                )
                        is Result.EmptyAnswer ->
                            ViewState(
                                    //state.items.find { it.id == result.id }.answer!! = result.answer
                                    state.items.map {
                                        if (it.id == result.id) {
                                            QuestionViewState(it.id, it.question, null)
                                        } else {
                                            it
                                        }
                                    }
                            )
                        else -> state
                    }
                }
                        .distinctUntilChanged()

            }



    data class QuestionViewState(val id: String, val question: String, val answer: String?)

    data class ViewState(
            val items: List<QuestionViewState> = listOf(
                    /*QuestionViewStateEvents(
                            "fd",
                            QuestionViewState("!2","123", null)
                    )*/
            )
    ) : UiState
}
