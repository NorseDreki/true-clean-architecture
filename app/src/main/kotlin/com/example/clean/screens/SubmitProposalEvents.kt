package com.example.clean.screens

import com.example.clean.screens2.ScreenEvents
import com.example.domain.submitProposal.SubmitProposal

class SubmitProposalEvents(val submitProposal: SubmitProposal) : ScreenEvents, ProposalSummaryEventHandler {

    override fun onCoverLetterClicked() {
        submitProposal.fromEvent(SubmitProposal.Command.ToCoverLetter)
    }

    override fun onClarifyingQuestionsClicked() {
        submitProposal.fromEvent(SubmitProposal.Command.ToClarifyingQuestions)
    }
}
