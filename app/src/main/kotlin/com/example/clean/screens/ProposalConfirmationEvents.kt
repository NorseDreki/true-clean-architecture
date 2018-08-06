package com.example.clean.screens

import com.example.clean.screens2.ScreenEvents
import com.example.domain.submitProposal.ProposalConfirmation

class ProposalConfirmationEvents(
        val proposalConfirmation: ProposalConfirmation
) : ScreenEvents {

    fun onDoneClicked() {
        println("done clicked")
        proposalConfirmation.fromEvent(ProposalConfirmation.Command.Dismiss)
    }
}
