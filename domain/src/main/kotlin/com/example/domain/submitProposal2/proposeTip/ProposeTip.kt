package com.example.domain.submitProposal2.proposeTip

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.proposeTip.ProposeTip.*

class ProposeTip : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        //from UI
        data class UpdateBid(val bid: Int) : Command()
        data class UpdateEngagement(val engagement: String) : Command()
    }

    sealed class Result : UiResult {

        sealed class Validation : Result() {
            object OK : Validation()
            object Failed : Validation()
        }

        data class ItemGreetingLoaded(val message: String) : Result()
        data class EngagementsLoaded(val message: String) : Result()

        data class BidValid(val bid: Int, val fee: Int) : Result()
        object BidEmpty : Result()
        data class BidRangeExceeded(val bid: Int, val min: Int, val max: Int) : Result()


        data class EngagementSelected(val engagement: String): Result()
        object EngagementNotSelected: Result()

        object EngagementNotRequired: Result()
    }

    data class ViewState(
            val itemDescription: String = "",
            val bid: Int = 0,
            val isRangeError: Boolean = false,
            val fee: Int = 0
    ) : UiState



    override val processor = Processor()
    override val reducer = Reducer()
}
