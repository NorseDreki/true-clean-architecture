package com.example.clean.screens2.dummyConfirmation

import com.example.clean.screens2.ScreenEvents
import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.DummyConfirmation

class DummyConfirmationEvents(
        val dummyConfirmation: DummyConfirmation
) : ScreenEvents {

    fun onDoneClicked() {
        println("done clicked")
        dummyConfirmation.extraCommand(DummyConfirmation.Command.Dismiss)
    }
}
