package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.Thunk
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import io.reactivex.Observable

class AnsweredQuestionsCountThunk : Thunk<Result, Result> {

    data class AnsweredQuestionsCount(
            val totalQuestions: Int = 0,
            val answeredQuestions: MutableSet<String> = mutableSetOf()
    )

    override fun apply(upstream: Observable<Result>) =
        upstream
                .scan(AnsweredQuestionsCount()) { state, result ->
                    when (result) {
                        is Result.QuestionsLoaded -> {
                            state.copy(
                                    totalQuestions = result.questions.size
                            )
                        }
                        Result.NotRequired -> state
                        is Result.AnswerValid -> {
                            state.copy(answeredQuestions = state.answeredQuestions.apply { add(result.questionId) })
                        }
                        is Result.AnswerEmpty -> {
                            state.copy(answeredQuestions = state.answeredQuestions.apply { remove(result.questionId) })
                        }
                        is Result.AnsweredQuestionsCount ->
                                throw IllegalStateException("Result ($result) must not appear here")
                    }
                }
                .skip(1)
                .map {
                    Result.AnsweredQuestionsCount(it.answeredQuestions.size, it.totalQuestions)
                }
                .cast(Result::class.java)!!
}
