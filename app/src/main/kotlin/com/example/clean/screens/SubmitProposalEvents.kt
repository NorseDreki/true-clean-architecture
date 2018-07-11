package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.R
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.ProposalSummaryEventHandler
import com.example.domain.submitProposal.SubmitProposal
import me.tatarka.bindingcollectionadapter2.ItemBinding

class SubmitProposalEvents(val submitProposal: SubmitProposal) : ScreenEvents, ProposalSummaryEventHandler {

    override fun handleProposalSummaryEvent(event: ProposalSummaryEventHandler.Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val itemBinding
            = ItemBinding.of<ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.answered_question_item)


    override fun onCoverLetterClicked() {
        submitProposal.fromEvent(SubmitProposal.Command.ToCoverLetter)
    }

    override fun onClarifyingQuestionsClicked() {
        submitProposal.fromEvent(SubmitProposal.Command.ToClarifyingQuestions)
    }
}
