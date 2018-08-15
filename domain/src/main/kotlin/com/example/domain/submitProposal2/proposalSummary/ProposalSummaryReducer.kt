package com.example.domain.submitProposal2.proposalSummary

import com.example.domain.Reducer
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.common.toViewState
import com.example.domain.submitProposal2.common.updateAnswer
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import io.reactivex.Observable

class ProposalSummaryReducer : Reducer<UiResult, ProposalSummaryViewState> {

    override fun apply(upstream: Observable<UiResult>) =
            upstream.scan(ProposalSummaryViewState()) { state, result ->
                when (result) {
                    CoverLetter.Result.NotRequired -> state.copy(
                            hasCoverLetter = false
                    )
                    is CoverLetter.Result.Valid -> {
                        state.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = true
                        )
                    }
                    is CoverLetter.Result.LengthExceeded ->
                        state.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = false
                        )
                    CoverLetter.Result.Empty ->
                        state.copy(
                                coverLetter = "",
                                isCoverLetterValid = false
                        )

                    ClarifyingQuestions.Result.NotRequired -> {
                        state.copy(
                                hasQuestions = false
                        )
                    }
                    is ClarifyingQuestions.Result.QuestionsLoaded -> {
                        state.copy(
                                questions = result.questions.toViewState()
                        )
                    }
                    is ClarifyingQuestions.Result.AnswerValid -> {
                        state.copy(
                                questions = state.questions.updateAnswer(result.questionId, result.answer)
                        )
                    }
                    is ClarifyingQuestions.Result.AnswerEmpty -> {
                        state.copy(
                                questions = state.questions.updateAnswer(result.questionId, "")
                        )
                    }
                    is ClarifyingQuestions.Result.AnsweredQuestionsCount -> state.copy(
                            answeredQuestions = result.answeredCount,
                            totalQuestions = result.totalCount,
                            areQuestionsValid = result.answeredCount == result.totalCount
                    )
                    else -> {
                        state
                    }
                }
            }
                    .distinctUntilChanged()!!
}
