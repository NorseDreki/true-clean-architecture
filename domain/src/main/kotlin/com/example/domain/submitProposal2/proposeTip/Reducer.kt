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
                            state.copy(tip = "", isRangeError = false)

                    is Result.TipValid ->
                        state.copy(tip = result.tip.toString(), isRangeError = false)

                    is Result.TipRangeExceeded ->
                        state.copy(tip = result.tip.toString(), isRangeError = true, minTip = result.min, maxTip = result.max)

                    is Result.FeeCalculatorLoaded ->
                            state.copy(isFeeLoading = false)

                    is Result.FeeCalculated ->
                            state.copy(tipWithFee = result.tipWithFee.toString(), isFeeLoading = false)

                    is Result.FeeCleared ->
                        state.copy(tipWithFee = "")

                    //to emit nothing is better
                    else -> state
                }
            }!!
}
