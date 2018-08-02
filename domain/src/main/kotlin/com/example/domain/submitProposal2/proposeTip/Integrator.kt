package com.example.domain.submitProposal2.proposeTip

import com.example.domain.framework.WithMemoized
import com.example.domain.submitProposal2.fees.FeesCalculator
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Integrator : ObservableTransformer<Result, Result> {

    data class State(
            val feesCalculator: FeesCalculator? = null,
            val tipWithFee: Int? = null
    )

    override fun apply(upstream: Observable<Result>) =
            upstream
                    .filter { it is Result.FeeCalculatorLoaded ||
                            it is Result.TipValid ||
                            it is Result.TipNotEntered ||
                            it is Result.TipRangeExceeded
                    }
                    .compose(WithMemoized<Result>())
                    .map {
                        when (it.current) {
                            is Result.TipValid -> {
                                //calculate
                                val calculator
                                        = (it.memo as Result.FeeCalculatorLoaded).feeCalculator

                                Result.FeeCalculated(it.current.tip + 123)
                            }
                            is Result.TipNotEntered -> {
                                Result.FeeCleared
                            }
                            is Result.TipRangeExceeded -> {
                                Result.FeeCleared
                            }
                            else -> throw IllegalStateException("not expected")
                        }
                    }!!
            /*upstream.scan(State()) { state, result ->
                when (result) {
                    is Result.FeeCalculatorLoaded -> {
                        //calculate
                        state.copy(result.feeCalculator)
                    }
                    else -> state
                }
            }
                    .skipWhile { it.feesCalculator == null }

            }!!*/

}