package com.example.domain.submitProposal2.suggestedTip

import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.Result
import com.example.domain.submitProposal2.suggestedTip.SuggestedTip.ViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =

            upstream.scan(ViewState()) { state, result ->
                when (result) {

                    Result.SuggestionAccepted ->
                            state.copy(isVisible = false)

                    Result.SuggestionNotAvailable ->
                            state.copy(isVisible = false)

                    is Result.SuggestionLoaded ->
                            state.copy(suggestedTip = result.suggestedTip.toString(), isVisible = true)

                    //to emit nothing is better
                    else -> state
                }
            }!!
}
