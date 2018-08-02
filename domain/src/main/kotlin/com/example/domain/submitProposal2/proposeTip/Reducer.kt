package com.example.domain.submitProposal2.proposeTip

import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import com.example.domain.submitProposal2.proposeTip.ProposeTip.ViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =

            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    Result.TipNotEntered ->
                            state.copy(tip = 0, isRangeError = false)

                    is Result.TipValid ->
                        state.copy(tip = result.tip, isRangeError = false)

                    is Result.TipRangeExceeded ->
                        state.copy(tip = result.tip, isRangeError = true, minTip = result.min, maxTip = result.max)

                    is Result.FeeCalculated ->
                            state.copy(tipWithFee = result.tipWithFee, isFeeLoading = false)

                    is Result.FeeCleared ->
                        state.copy(tipWithFee = 0, isFeeLoading = true)

                    //to emit nothing is better
                    else -> state
                }
            }!!
}
