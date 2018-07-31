package com.example.domain.submitProposal2

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.fees.FeesResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class LoopbackCommands : ObservableTransformer<UiResult, UiCommand> {

    override fun apply(upstream: Observable<UiResult>) =
            upstream.flatMap {
                when (it) {
                    is SubmitProposal.Result.ProposalLoaded -> {
                        //hide panel
                        Observable.fromArray(
                                CoverLetter.Command.DATA(it.itemOpportunity),
                                ClarifyingQuestions.Command.INIT(it.itemOpportunity),
                                DoSubmitProposal.Command.DATA(it.itemOpportunity)
                        )
                        //AnchoredPanel.Command.Expand
                    }
                    SubmitProposal.Result.ProposalUpdated -> {
                        Observable.empty()
                    }
                    SubmitAllowedResult.Enabled -> Observable.just(
                            DoSubmitProposal.Command.ToggleSubmitEnabled(true)
                    )
                    SubmitAllowedResult.Disabled -> {
                        Observable.just(DoSubmitProposal.Command.ToggleSubmitEnabled(false))
                    }
                    SubmitProposal.Result.ProposalRemoved -> {
                        //hide panel
                        Observable.empty()

                    }
                    is FeesResult.CalculatorLoaded -> {
                        //ProposeTerms.Command.DATA
                        Observable.empty()
                    }
                    is DoSubmitProposal.Result.Success -> Observable.just(
                            SubmitProposal.Command.RemoveProposal
                    )
                    else -> {
                        Observable.empty()
                    }
                }
            }!!
}
