package com.example.domain.submitProposal2.anchorablePanelSubmitProposal

import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposalSummary.ProposalSummaryViewState

data class ReviewViewState(
        val proposalSummary: ProposalSummaryViewState,
        val doSubmitProposal: DoSubmitProposal.ViewState
)
