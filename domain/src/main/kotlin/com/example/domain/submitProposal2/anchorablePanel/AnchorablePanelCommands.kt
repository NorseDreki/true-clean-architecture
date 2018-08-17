package com.example.domain.submitProposal2.anchorablePanel

import com.example.domain.UiCommand

sealed class AnchorablePanelCommands : UiCommand {
    object Anchor : AnchorablePanelCommands()
    object Hide : AnchorablePanelCommands()
    object Expand : AnchorablePanelCommands()
    object Collapse : AnchorablePanelCommands()
}
