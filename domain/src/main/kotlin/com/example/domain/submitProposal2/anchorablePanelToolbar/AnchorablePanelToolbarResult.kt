package com.example.domain.submitProposal2.anchorablePanelToolbar

import com.example.domain.UiResult

sealed class AnchorablePanelToolbarResult : UiResult {
    data class TitleChanged(val title: String) : AnchorablePanelToolbarResult()
    object DiscardRequested: AnchorablePanelToolbarResult()
    object NextRequested: AnchorablePanelToolbarResult()
    object PreviousRequested: AnchorablePanelToolbarResult()
    object CollapseRequested: AnchorablePanelToolbarResult()
    object ExpandRequested: AnchorablePanelToolbarResult()
}
