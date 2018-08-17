package com.example.domain.submitProposal2.anchorablePanelSubmitProposal

import com.example.domain.submitProposal2.anchorablePanel.AnchorablePanelViewState
import com.example.domain.submitProposal2.anchorablePanelToolbar.AnchorablePanelToolbarViewState

data class PanelSubmitProposalViewState(
        val anchorablePanel: AnchorablePanelViewState,
        val anchorablePanelToolbar: AnchorablePanelToolbarViewState,
        val submitProposalPages: SubmitProposalPages
)