package com.example.clean.screens

import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.DoSubmitProposal

class DoSubmitProposalEvents(
        val doSubmitProposal: DoSubmitProposal
) : ScreenEvents {

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
