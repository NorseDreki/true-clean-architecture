package com.example.domain.submitProposal2.proposeTip

import com.example.domain.framework.WithResults
import com.example.domain.submitProposal2.fees.FeesCalculator
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Command
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

class Processor : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .flatMap {

                        when (it) {
                            is Command.DATA -> {
                                Observable.fromArray(
                                        Result.ItemGreetingLoaded("greeting"),
                                        calculate(it.itemOpportunity.proposal.bid)
                                ).flatMap {
                                    Observable.fromCallable {
                                        Thread.sleep(3000)
                                        Result.FeeCalculatorLoaded(object : FeesCalculator {})
                                    }.subscribeOn(Schedulers.io())
                                }

                            }
                            is Command.UpdateTip -> {
                                Observable.just(calculate(it.tip))
                            }
                            is Command.ForceRecalculateFee -> {
                                Observable.fromCallable {
                                    Thread.sleep(3000)
                                    Result.FeeCalculatorLoaded(object : FeesCalculator {})
                                }.subscribeOn(Schedulers.io())
                            }
                        }
                    }
                    .compose(WithResults(Integrator()))!!

    fun calculate(tip: Int): Result {

        return when (tip) {
            0 -> Result.TipNotEntered
            else -> when (tip) {
                in 101..999 -> {
                    val fee = tip / 10
                    Result.TipValid(tip)
                }
                else -> Result.TipRangeExceeded(tip, 100, 1000)
            }
        }
    }

    fun duration(): Result {
        return Result.EngagementNotSelected
    }
}
