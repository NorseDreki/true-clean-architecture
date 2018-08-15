package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.*

class SuggestedTip(
        userSuggestion: UserSuggestion
) : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class START(val currentTip: Int) : Command(), UiDataCommand

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


    override val processor = SuggestedTipProcessor(userSuggestion)
    override val reducer = SuggestedTipReducer()
}
