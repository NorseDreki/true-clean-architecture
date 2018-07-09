package com.example.clean.screens

class ProposalSummaryScreenEvents(val handler: ProposalSummaryEventHandler) : ScreenEvents {


    fun onSubmitClicked() {
        handler.handleProposalSummaryEvent(ProposalSummaryEventHandler.Event.OnSubmit)
    }
}
