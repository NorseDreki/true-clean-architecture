package com.example.domain.submitProposal2.anchorablePanelSubmitProposal

import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter

data class SubmitProposalPages(
        val index: Int,
        val tipViewState: TipViewState,
        val coverLetter: CoverLetter.ViewState,
        val clarifyingQuestions: ClarifyingQuestions.ViewState,
        val review: ReviewViewState
)
