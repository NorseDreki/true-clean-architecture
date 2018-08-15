package com.example.domain.submitProposal2.proposeTip

import com.example.domain.Thunk
import com.example.domain.framework.WithMemoized
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result.*
import io.reactivex.Observable

class TotalCalculationThunk : Thunk<Result, Result> {

    override fun apply(upstream: Observable<Result>) =
            upstream
                    .filter { it is Result.FeeCalculatorLoaded ||
                            it is Result.TipValid ||
                            it is Result.TipNotEntered ||
                            it is Result.TipRangeExceeded
                    }
                    .compose(WithMemoized<Result>())
                    .filter { it.current !is FeeCalculatorLoaded }
                    .map {
                        when (it.current) {
                            is TipValid -> {
                                val calculator
                                        = (it.memo as FeeCalculatorLoaded).feeCalculator

                                TotalCalculated(it.current.tip + 123)
                            }
                            TipNotEntered -> {
                                TotalCleared
                            }
                            is TipRangeExceeded -> {
                                TotalCleared
                            }
                            else -> throw IllegalStateException("not expected ${it.current}")
                        }
                    }!!
}
