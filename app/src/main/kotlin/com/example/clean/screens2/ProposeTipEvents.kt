package com.example.clean.screens2

import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.proposeTip.ProposeTip

class ProposeTipEvents(
        val proposeTip: ProposeTip
) {

    fun onTipUpdated(text: CharSequence) {
        println("onTipUpdated")
        proposeTip.extraCommand(ProposeTip.Command.UpdateTip(text.toString().toInt()))
    }
}
