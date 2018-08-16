package com.example.domain.submitProposal2.anchorablePanelToolbar

import com.example.domain.UiState

data class AnchorablePanelToolbarViewState(
        val title: String,
        val respondsToClicks: Boolean,
        val discardVisible: Boolean,
        val collapseVisible: Boolean,
        val backVisible: Boolean,
        val nextVisible: Boolean,
        val isAtBottom: Boolean
) : UiState
