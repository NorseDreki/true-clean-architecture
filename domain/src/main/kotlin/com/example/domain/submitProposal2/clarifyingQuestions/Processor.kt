package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.UiCommand
import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Processor : ObservableTransformer<ClarifyingQuestions.Command, ClarifyingQuestions.Result> {

    override fun apply(upstream: Observable<ClarifyingQuestions.Command>): ObservableSource<ClarifyingQuestions.Result> {
        return upstream.compose(paProcessor)
                .doOnNext { println("RESCQ " + it) }
                .cast(ClarifyingQuestions.Result::class.java)
                .publish { shared ->
                    Observable.merge(
                            shared,
                            shared.doOnNext { println("input222") }.compose(paAnsweredProcessor)//.skip(1)
                    ).doOnNext { println("COMB $it") }
                }
    }
    data class AllQuestionsAnswered(
            val totalQuestions: Int? = null,
            val answeredQuestions: MutableSet<String> = mutableSetOf("1234")
    )

    val paAnsweredProcessor =
            ObservableTransformer<ClarifyingQuestions.Result, ClarifyingQuestions.Result> { t ->
                t.doOnNext { println("111 $it") }
                        .scan(AllQuestionsAnswered()) { state, result ->
                            when (result) {
                                is ClarifyingQuestions.Result.QuestionsLoaded -> {
                                    state.copy(result.questions.size, mutableSetOf())
                                }
                                is ClarifyingQuestions.Result.NoQuestionsRequired -> {
                                    AllQuestionsAnswered()
                                    //state.copy(0, mutableSetOf())
                                }
                                is ClarifyingQuestions.Result.ValidAnswer -> {
                                    state.copy(answeredQuestions = state.answeredQuestions.apply { add(result.id) })
                                }
                                is ClarifyingQuestions.Result.EmptyAnswer -> {
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
                                Observable.empty<ClarifyingQuestions.Result>()
                            } else {
                                Observable.just(ClarifyingQuestions.Result.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size))
                            }
                        }

            }


    val paProcessor =
            ObservableTransformer<UiCommand, UiResult> { t ->
                t.flatMap {
                    when (it) {
                        is ClarifyingQuestions.Command.INIT -> {
                            val questions = it.itemOpportunity.itemDetails.questions
                            val answers = it.itemOpportunity.proposal.questionAnswers

                            when {
                                questions != null && answers != null -> {
                                    Observable
                                            .just(ClarifyingQuestions.Result.QuestionsLoaded(questions))
                                            .cast(UiResult::class.java)
                                            .mergeWith(
                                                    Observable
                                                            .fromIterable(answers)
                                                            .map { ClarifyingQuestions.Result.ValidAnswer(it.id, it.answer) }
                                            )
                                    //Observable.fromArray(Result.Questions(questions))
                                }
                                questions != null -> Observable.just(ClarifyingQuestions.Result.QuestionsLoaded(questions))
                                else -> Observable.just(ClarifyingQuestions.Result.NoQuestionsRequired)
                            }
                        }
                        is ClarifyingQuestions.Command.UpdateAnswer -> {
                            val validated = it.answer.trim()


                            if (validated.isNotEmpty()) {
                                Observable.just(ClarifyingQuestions.Result.ValidAnswer(it.id, validated))
                            } else {
                                Observable.just(ClarifyingQuestions.Result.EmptyAnswer(it.id))
                            }
                        }
                        else -> throw IllegalStateException("sdf")
                    }
                }
            }
}
