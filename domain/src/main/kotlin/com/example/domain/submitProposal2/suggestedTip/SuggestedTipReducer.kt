package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.Reducer
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result.*
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.ViewState
import io.reactivex.Observable

class SuggestedTipReducer : Reducer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =
            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    SuggestionAccepted ->
                            state.copy(isVisible = false)

                    SuggestionNotAvailable ->
                            state.copy(isVisible = false)

                    is SuggestionLoaded ->state.copy(
                            suggestedTip = result.suggestedTip.toString(),
                            isVisible = true
                    )
                }
            }!!
}
