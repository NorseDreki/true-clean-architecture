package com.example.domain.submitProposal2.anchorablePanelSubmitProposal

import com.example.domain.submitProposal2.proposeTip.ProposeTip
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip

data class TipViewState(
        val proposeTip: ProposeTip.ViewState,
        val suggestedTip: SuggestedTip.ViewState
)
