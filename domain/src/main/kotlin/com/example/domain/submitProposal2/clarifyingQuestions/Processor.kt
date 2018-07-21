package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Command
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Processor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream.compose(paProcessor)
                    .publish { shared ->
                        Observable.merge(
                                shared,
                                shared.compose(paAnsweredProcessor)
                        )
                    }


    data class AllQuestionsAnswered(
            val totalQuestions: Int? = null,
            val answeredQuestions: MutableSet<String> = mutableSetOf("1234")
    )

    val paAnsweredProcessor =
            ObservableTransformer<Result, Result.AllQuestionsAnswered> { t ->
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
                            //else -> throw IllegalStateException("sdf")
                                is ClarifyingQuestions.Result.AllQuestionsAnswered -> TODO()
                            }
                        }
                        .skip(1)
                        .flatMap {
                            //implicitly handles "no questions" case
                            if (it.totalQuestions == null) {
                                println("value! $it")
                                Observable.empty<Result.AllQuestionsAnswered>()
                            } else {
                                Observable.just(Result.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size))
                            }
                        }

            }


    val paProcessor =
            ObservableTransformer<Command, Result> { t ->
                t.flatMap {
                    when (it) {
                        is Command.INIT -> {
                            val questions = it.itemOpportunity.itemDetails.questions
                            val answers = it.itemOpportunity.proposal.questionAnswers

                            when {
                                questions != null && answers != null -> {
                                    Observable
                                            .just(Result.QuestionsLoaded(questions))
                                            .cast(Result::class.java)
                                            .mergeWith(
                                                    Observable
                                                            .fromIterable(answers)
                                                            .map { Result.ValidAnswer(it.id, it.answer) }
                                            )
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
                    }
                }
            }
}
