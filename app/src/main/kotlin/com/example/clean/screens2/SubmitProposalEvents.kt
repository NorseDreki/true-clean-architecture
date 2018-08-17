package com.example.clean.screens2

import com.example.clean.framework.ScreenEvents
import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.SubmitProposal

class SubmitProposalEvents(val submitProposal: SubmitProposal) : ScreenEvents, ProposalSummaryEventHandler {

    override fun onCoverLetterClicked() {
        submitProposal.extraCommand(SubmitProposal.Command.ToCoverLetter)
    }

    override fun onClarifyingQuestionsClicked() {
        submitProposal.extraCommand(SubmitProposal.Command.ToClarifyingQuestions)
    }
}
