package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.submitProposal2.proposeTip.Processor
import com.example.domain.submitProposal2.proposeTip.ProposeTip.*
import com.example.domain.submitProposal2.proposeTip.Reducer

class SuggestTip : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class DATA(val currentTip: Int) : Command(), UiDataCommand

        object LearnMore : Command()
        object AcceptSugestion : Command()
    }

    sealed class Result : UiResult {
        data class SuggestionLoaded(val suggestedTip: Int) : Result()

        object SuggestionNotAvailable : Result()

        object SuggestionAccepted : Result()
    }

    data class ViewState(
            val suggestedTip: String = "",
            val isVisible: Boolean = false
    ) : UiState


    override val processor = Processor()
    override val reducer = Reducer()
}
