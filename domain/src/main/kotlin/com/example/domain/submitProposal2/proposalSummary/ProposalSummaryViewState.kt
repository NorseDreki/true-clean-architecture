package com.example.domain.submitProposal2.proposalSummary

import com.example.domain.UiState
import com.example.domain.submitProposal2.common.QuestionViewState

data class ProposalSummaryViewState(
        val hasCoverLetter: Boolean = true,
        val hasQuestions: Boolean = true,

        val coverLetter: String = "",
        val isCoverLetterValid: Boolean = false,

        val answeredQuestions: Int = 0,
        val totalQuestions: Int = 0,
        val areQuestionsValid: Boolean = false,

        val questions: List<QuestionViewState> = listOf()
) : UiState
