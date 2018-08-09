package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.ViewState
import com.example.domain.submitProposal2.common.toViewState
import com.example.domain.submitProposal2.common.updateAnswer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ClarifyingQuestionsReducer : ObservableTransformer<Result, ViewState> {

    override fun apply(it: Observable<Result>) =
            it.scan(ViewState()) { state, result ->
                when (result) {
                    Result.NotRequired ->
                        ViewState(isVisible = false)

                    is Result.QuestionsLoaded -> state.copy(
                            questions = result.questions.toViewState()
                    )
                    is Result.AnswerValid -> state.copy(
                            questions = state.questions.updateAnswer(result.questionId, result.answer)
                    )
                    is Result.AnswerEmpty -> state.copy(
                            questions = state.questions.updateAnswer(result.questionId, "")
                    )
                    is Result.AnsweredQuestionsCount -> state
                }
            }
                    .distinctUntilChanged()!!
}
