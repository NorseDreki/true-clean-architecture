package com.example.domain.submitProposal2.fees

import com.example.domain.UiCommand

sealed class FeesCommands : UiCommand {
    object LoadCalculator : FeesCommands()
    object LoadUpdatedCalculator : FeesCommands()
}