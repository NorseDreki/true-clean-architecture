package com.example.domain.submitProposal2.proposeTip

import com.example.domain.Processor
import com.example.domain.framework.WithResults
import com.example.domain.submitProposal2.fees.FeesCalculator
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Command
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Command.*
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result
import com.example.domain.submitProposal2.proposeTip.ProposeTip.Result.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

class ProposeTipProcessor : Processor<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .compose(processor)
                    .compose(WithResults(TotalCalculationThunk()))!!

    val processor =
            ObservableTransformer<Command, Result> {
                it.flatMap {
                    when (it) {
                        is START -> {
                            Observable.fromArray(
                                    ItemGreetingLoaded("greeting"),
                                    calculate(it.itemOpportunity.proposal.bid)
                            ).flatMap {
                                calculatorLoaded()
                            }

                        }
                        is UpdateTip -> {
                            Observable.just(calculate(it.tip))
                        }
                        ForceRecalculateTotal -> {
                            calculatorLoaded()
                        }
                    }
                }
            }

    private fun calculatorLoaded() =
        Observable.fromCallable {
            Thread.sleep(3000)
            FeeCalculatorLoaded(object : FeesCalculator {})
        }.subscribeOn(Schedulers.io())

    private fun calculate(tip: Int) =
        when (tip) {
            0 -> TipNotEntered
            else -> when (tip) {
                in 101..999 -> {
                    val fee = tip / 10
                    TipValid(tip)
                }
                else -> TipRangeExceeded(tip, 100, 1000)
            }
        }
}
