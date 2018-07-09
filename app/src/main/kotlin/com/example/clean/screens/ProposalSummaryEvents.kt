package com.example.clean.screens

import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.ProposalSummaryEventHandler

class ProposalSummaryEvents(val handler: ProposalSummaryEventHandler, val dialogEvents: DialogEvents) : ScreenEvents {


    fun onSubmitClicked() {
        println("submit clicked")
        handler.handleProposalSummaryEvent(ProposalSummaryEventHandler.Event.OnSubmit)
    }
}
