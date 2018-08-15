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
                            state.copy(tip = "", isTipRangeError = false)

                    is Result.TipValid ->
                        state.copy(tip = result.tip.toString(), isTipRangeError = false)

                    is Result.TipRangeExceeded ->
                        state.copy(tip = result.tip.toString(), isTipRangeError = true, minTip = result.min, maxTip = result.max)

                    is Result.FeeCalculatorLoaded ->
                            state.copy(isTotalCalculationPending = false)

                    is Result.TotalCalculated ->
                            state.copy(total = result.total.toString(), isTotalCalculationPending = false)

                    is Result.TotalCleared ->
                        state.copy(total = "")

                    //to emit nothing is better
                    else -> state
                }
            }!!
}
