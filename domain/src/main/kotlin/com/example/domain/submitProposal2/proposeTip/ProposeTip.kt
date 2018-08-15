package com.example.domain.submitProposal2.proposeTip

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.framework.Memoizable
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.fees.FeesCalculator
import com.example.domain.submitProposal2.proposeTip.ProposeTip.*

class ProposeTip : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class START(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        object ForceRecalculateTotal : Command()

        data class UpdateTip(val tip: Int) : Command()
    }

    sealed class Result : UiResult {
        data class ItemGreetingLoaded(val message: String) : Result()

        data class TipValid(val tip: Int) : Result()
        object TipNotEntered : Result()
        data class TipRangeExceeded(val tip: Int, val min: Int, val max: Int) : Result()

        data class TotalCalculated(val total: Int) : Result()
        object TotalCleared : Result()

        data class FeeCalculatorLoaded(val feeCalculator: FeesCalculator) : Result(), Memoizable
    }

    data class ViewState(
            val itemGreeting: String = "",
            val tip: String = "",
            val isTipRangeError: Boolean = false,
            val minTip: Int = 0,
            val maxTip: Int = 0,
            val total: String = "",
            val isTotalCalculationPending: Boolean = true
    ) : UiState


    override val processor = Processor()
    override val reducer = ProposeTipReducer()
}
