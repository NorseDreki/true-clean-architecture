package com.example.domain.submitProposal2.anchorablePanelToolbar

import com.example.domain.UiCommand

sealed class AnchorablePanelToolbarCommand : UiCommand {
    data class ChangeTitle(val title: String) : AnchorablePanelToolbarCommand()
    object Next : AnchorablePanelToolbarCommand()
    object Previous : AnchorablePanelToolbarCommand()
    object ClickToolbar : AnchorablePanelToolbarCommand()
    object Discard : AnchorablePanelToolbarCommand()
    object Collapse : AnchorablePanelToolbarCommand()
    object Expand : AnchorablePanelToolbarCommand()
}
