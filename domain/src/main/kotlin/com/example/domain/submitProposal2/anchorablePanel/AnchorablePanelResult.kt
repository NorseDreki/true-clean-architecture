package com.example.domain.submitProposal2.anchorablePanel

import com.example.domain.UiResult

sealed class AnchorablePanelResult : UiResult {
    data class PanelStateChanged(val panelState: PanelState) : AnchorablePanelResult()
    data class PageChanged(val page: Int) : AnchorablePanelResult()
    data class TitleChanged(val title: String) : AnchorablePanelResult()
    object Discard: AnchorablePanelResult()
}
