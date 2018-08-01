package com.example.domain.submitProposal2.proposeTip

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.fees.FeesCalculator
import com.example.domain.submitProposal2.proposeTip.ProposeTip.*

class ProposeTip : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        object ForceRecalculateFee : Command()

        //from UI
        data class UpdateTip(val tip: Int) : Command()
    }

    sealed class Result : UiResult {

        sealed class Validation : Result() {
            object OK : Validation()
            object Failed : Validation()
        }

        data class ItemGreetingLoaded(val message: String) : Result()
        data class EngagementsLoaded(val message: String) : Result()

        data class TipValid(val tip: Int) : Result()
        object TipNotEntered : Result()
        data class TipRangeExceeded(val tip: Int, val min: Int, val max: Int) : Result()

        data class FeeCalculated(val tipWithFee: Int) : Result()
        data class FeeCalculatorLoaded(val feeCalculator: FeesCalculator) : Result()


        //calculate connects
        data class EngagementSelected(val engagement: String): Result()
        object EngagementNotSelected: Result()

        object EngagementNotRequired: Result()
    }

    data class ViewState(
            val itemDescription: String = "",
            val tip: Int = 0,
            val isRangeError: Boolean = false,
            val minTip: Int = 0,
            val maxTip: Int = 0,
            val tipWithFee: Int = 0,
            val isFeeLoading: Boolean = true
    ) : UiState


    override val processor = Processor()
    override val reducer = Reducer()
}
