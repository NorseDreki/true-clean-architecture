package com.example.clean.screens2

import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.common.DialogEvents
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal


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
        doSubmitProposal.extraCommand(DoSubmitProposal.Command.DoSubmit)
    }
}
