package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.R
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.ProposalSummaryEventHandler
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ProposalSummaryEvents(val handler: ProposalSummaryEventHandler, val dialogEvents: DialogEvents) : ScreenEvents {

    val itemBinding = ItemBinding.of<ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.answered_question_item)
            //.bindExtra(BR.listener, this)!!

    fun onSubmitClicked() {
        println("submit clicked")
        handler.handleProposalSummaryEvent(ProposalSummaryEventHandler.Event.OnSubmit)
    }
}
