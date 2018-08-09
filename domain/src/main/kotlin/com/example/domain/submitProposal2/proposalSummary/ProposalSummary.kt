package com.example.domain.submitProposal2.proposalSummary

import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.common.JobTypeViewState
import com.example.domain.submitProposal2.SubmitProposal
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.common.QuestionViewState
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import io.reactivex.ObservableTransformer

data class JobInfoViewState(
        val title: String,
        val jobType: JobTypeViewState
) : UiState {
    companion object {
        fun init() = JobInfoViewState("", JobTypeViewState.init())
    }
}

val psReducer =
        ObservableTransformer<UiResult, ProposalSummaryViewState> {
            it.scan(ProposalSummaryViewState.init()) { state, result ->
                val s = state as ProposalSummaryViewState
                when (result) {
                /*is CoverLetter.Result.NoCoverLetterRequired -> {
                    s.copy(hasCoverLetter = false)
                }*/
                    is SubmitProposal.Result.ProposalLoaded -> {
                        s.copy(hasCoverLetter = result.itemOpportunity.itemDetails.isCoverLetterRequired)
                    }
                    is CoverLetter.Result.Valid -> {
                        s.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = true
                        )
                    }
                    is CoverLetter.Result.LengthExceeded ->
                        s.copy(
                                coverLetter = result.coverLetter,
                                isCoverLetterValid = false
                        )
                    CoverLetter.Result.Empty ->
                        s.copy(
                                coverLetter = "",
                                isCoverLetterValid = false
                        )
                    ClarifyingQuestions.Result.NotRequired -> {
                        s.copy(
                                hasQuestions = false
                        )
                    }
                    is ClarifyingQuestions.Result.QuestionsLoaded -> {
                        s.copy(
                                hasQuestions = true,
                                questions = result.questions.map {
                                    QuestionViewState(it.id, it.question, null)
                                },
                                totalQuestions = result.questions.size
                        )
                    }
                    is ClarifyingQuestions.Result.AnswerValid -> {
                        val count = s.answeredQuestions + 1
                        s.copy(
                                questions = s.questions.map {
                                    if (it.id == result.questionId)
                                        QuestionViewState(it.id, it.question, result.answer)
                                    else
                                        it
                                },
                                answeredQuestions = if (count > s.totalQuestions) s.totalQuestions else count,
                                areQuestionsValid = count == s.totalQuestions
                        )
                    }
                    is ClarifyingQuestions.Result.AnswerEmpty -> {
                        val count = s.answeredQuestions - 1
                        s.copy(
                                questions = s.questions.map {
                                    if (it.id == result.questionId)
                                        QuestionViewState(it.id, it.question, null)
                                    else
                                        it
                                },
                                answeredQuestions = count,
                                areQuestionsValid = false
                        )
                    }
                    else -> {
                        s
                    }
                }
            }
                    .distinctUntilChanged()
        }


data class ProposalSummaryViewState(
        val hasCoverLetter: Boolean,
        val hasQuestions: Boolean,

        val coverLetter: String,
        val isCoverLetterValid: Boolean,

        val answeredQuestions: Int,
        val totalQuestions: Int,
        val areQuestionsValid: Boolean,

        val questions: List<QuestionViewState>
) : UiState {
    companion object {
        fun init() = ProposalSummaryViewState(
                false, false, "", false,
                0, 0, false, listOf()
        )
    }
}
