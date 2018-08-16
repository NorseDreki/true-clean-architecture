package com.example.domain.submitProposal2.layouts

import com.example.domain.UiState
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposeTip.ProposeTip
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip

data class LinearLayoutViewState(
        val proposeTip: ProposeTip.ViewState,
        val suggestedTip: SuggestedTip.ViewState,
        val coverLetter: CoverLetter.ViewState,
        val clarifyingQuestions: ClarifyingQuestions.ViewState,
        val doSubmitProposal: DoSubmitProposal.ViewState
) : UiState
