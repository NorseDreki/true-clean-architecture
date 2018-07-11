package com.example.domain.submitProposal


interface ProposalSummaryEventHandler {

    sealed class Event {


        object OnSubmit : Event()
    }

    fun handleProposalSummaryEvent(event: Event)

    fun onCoverLetterClicked()

    fun onClarifyingQuestionsClicked()
}
