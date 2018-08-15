package com.example.domain.submitProposal2.proposeTip

import com.example.domain.Reducer
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result.*
import com.example.domain.submitProposal2.proposeTip.ProposeTip.ViewState
import io.reactivex.Observable

class ProposeTipReducer : Reducer<Result, ViewState> {

    override fun apply(upstream: Observable<Result>) =
            upstream.scan(ViewState()) { state, result ->
                when (result) {
                    TipNotEntered -> state.copy(
                            tip = "",
                            isTipRangeError = false
                    )
                    is TipValid -> state.copy(
                            tip = result.tip.toString(),
                            isTipRangeError = false
                    )
                    is TipRangeExceeded ->state.copy(
                            tip = result.tip.toString(),
                            isTipRangeError = true,
                            minTip = result.min,
                            maxTip = result.max
                    )
                    is FeeCalculatorLoaded ->state.copy(
                            isTotalCalculationPending = false
                    )
                    is TotalCalculated ->state.copy(
                            total = result.total.toString(),
                            isTotalCalculationPending = false
                    )
                    TotalCleared ->state.copy(
                            total = ""
                    )
                    is ItemGreetingLoaded -> state.copy(
                            itemGreeting = result.message
                    )
                }
            }
                    .distinctUntilChanged()!!
}
