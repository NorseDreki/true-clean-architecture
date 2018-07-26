package com.example.clean.screens2

import com.example.clean.screens.ScreenEvents
import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation

class ProposalConfirmationEvents(
        val proposalConfirmation: ProposalConfirmation
) : ScreenEvents {

    fun onDoneClicked() {
        println("done clicked")
        proposalConfirmation.extraCommand(ProposalConfirmation.Command.Dismiss)
    }

    fun onAnotherOneClicked() {
        println("another one")
        proposalConfirmation.extraCommand(ProposalConfirmation.Command.AnotherOne)
    }
}
