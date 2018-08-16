package com.example.domain.submitProposal2.anchorablePanel

import com.example.domain.UiCommand
import com.example.domain.UiState

sealed class AnchorablePanelCommands : UiCommand {
    object Anchor : AnchorablePanelCommands()
    object Hide : AnchorablePanelCommands()
    object Expand : AnchorablePanelCommands()
    object Collapse : AnchorablePanelCommands()
    object HandleHardwareBack : AnchorablePanelCommands()
    data class ChangeTitle(val title: String) : AnchorablePanelCommands()
    data class NavigateToPage(val page: Int) : AnchorablePanelCommands()
    sealed class NavigateTo() : AnchorablePanelCommands() {
        object NextPage : NavigateTo()
        object PreviousPage : NavigateTo()
        data class SpecificPage(val number: Int) : NavigateTo()
    }

    data class AssignPages(val pages: List<UiState>) : AnchorablePanelCommands()
}
