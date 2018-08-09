package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.Processor
import com.example.domain.framework.WithResults
import com.example.domain.models.AnsweredQuestion
import com.example.domain.models.Question
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Command
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ClarifyingQuestionsProcessor : Processor<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream.compose(processor)
                    .compose(WithResults(AnsweredQuestionsCountThunk()))!!

    val processor =
            ObservableTransformer<Command, Result> {
                it.flatMap {
                    when (it) {
                        is Command.START -> {
                            val questions = it.itemOpportunity.itemDetails.questions
                            val answers = it.itemOpportunity.proposal.questionAnswers

                            when {
                                questions != null && answers != null -> {
                                    loadAnswers(questions, answers)
                                }
                                questions != null ->
                                    Observable.just(Result.QuestionsLoaded(questions))
                                else ->
                                    Observable.just(Result.NotRequired)
                            }
                        }
                        is Command.UpdateAnswer -> {
                            validate(it)
                        }
                    }
                }
            }

    private fun loadAnswers(questions: List<Question>, answers: List<AnsweredQuestion>): Observable<Result> {
        return Observable
                .just(Result.QuestionsLoaded(questions))
                .cast(Result::class.java)
                .mergeWith(
                        Observable
                                .fromIterable(answers)
                                .map { Result.AnswerValid(it.id, it.answer) }
                )
    }

    private fun validate(it: Command.UpdateAnswer): Observable<Result> {
        val validated = it.answer.trim()

        return if (validated.isNotEmpty()) {
            Observable.just(Result.AnswerValid(it.questionId, validated))
        } else {
            Observable.just(Result.AnswerEmpty(it.questionId))
        }
    }
}
