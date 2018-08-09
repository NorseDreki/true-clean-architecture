package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.framework.WithResults
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Command
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Processor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream.compose(paProcessor)
                    .compose(WithResults(paAnsweredProcessor))!!

    data class AllQuestionsAnswered(
            val totalQuestions: Int? = null,
            val answeredQuestions: MutableSet<String> = mutableSetOf("1234")
    )

    val paProcessor =
            ObservableTransformer<Command, Result> { t ->
                t.flatMap {
                    when (it) {
                        is Command.START -> {
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
                                                            .map { Result.AnswerValid(it.id, it.answer) }
                                            )
                                }
                                questions != null -> Observable.just(Result.QuestionsLoaded(questions))
                                else -> Observable.just(Result.NotRequired)
                            }
                        }
                        is Command.UpdateAnswer -> {
                            val validated = it.answer.trim()

                            if (validated.isNotEmpty()) {
                                Observable.just(Result.AnswerValid(it.questionId, validated))
                            } else {
                                Observable.just(Result.AnswerEmpty(it.questionId))
                            }
                        }
                    }
                }
            }
}
