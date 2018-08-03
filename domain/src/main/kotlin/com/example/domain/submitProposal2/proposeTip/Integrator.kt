package com.example.domain.submitProposal2.proposeTip

import com.example.domain.framework.WithMemoized
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Integrator : ObservableTransformer<Result, Result> {

    override fun apply(upstream: Observable<Result>) =
            upstream
                    .filter { it is Result.FeeCalculatorLoaded ||
                            it is Result.TipValid ||
                            it is Result.TipNotEntered ||
                            it is Result.TipRangeExceeded
                    }
                    .doOnNext { println("FILTERED $it") }
                    .compose(WithMemoized<Result>())
                    .filter { it.current !is Result.FeeCalculatorLoaded }
                    .map {
                        println("INTEGRATOR $it")
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
                            else -> throw IllegalStateException("not expected ${it.current}")
                        }
                    }!!
}
