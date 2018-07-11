package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.R
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.DoSubmitProposal
import me.tatarka.bindingcollectionadapter2.ItemBinding

class DoSubmitProposalEvents(
        val doSubmitProposal: DoSubmitProposal
) : ScreenEvents {

    val itemBinding
            = ItemBinding.of<ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.answered_question_item)

    val dialogEvents = DialogEvents(
            {
                println("positive");
                onSubmitClicked()
            },
            { println("negative") }
    )

    fun onSubmitClicked() {
        println("submit clicked")
        doSubmitProposal.fromEvent(DoSubmitProposal.Command.DoSubmit)
    }
}
