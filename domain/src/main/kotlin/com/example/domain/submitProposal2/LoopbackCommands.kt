package com.example.domain.submitProposal2

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.fees.FeesResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class LoopbackCommands : ObservableTransformer<UiResult, UiCommand> {

    override fun apply(upstream: Observable<UiResult>): ObservableSource<UiCommand> {
        return upstream.flatMap {

            //memoize itemOpportunity here as well?
            when (it) {
                is SubmitProposal.Result.ProposalUpdated -> {
                    //println("112233 PROPOSAL UPDATED")
                    Observable.empty<UiCommand>()
                }
                is SubmitAllowedResult.Enabled -> {
                    //Observable.just(ProposalSummary.Command.ToggleSubmitEnabled(true))
                    println("SUBMIT ENABLED!")

                    Observable.just(DoSubmitProposal.Command.ToggleSubmitEnabled(true))
                }
                is SubmitAllowedResult.Disabled -> {
                    //Observable.just(ProposalSummary.Command.ToggleSubmitEnabled(true))

                    Observable.just(DoSubmitProposal.Command.ToggleSubmitEnabled(false))
                }

                is SubmitProposal.Result.ProposalLoaded -> {
                    //hide panel
                    Observable.fromArray(
                            CoverLetter.Command.DATA(it.itemOpportunity!!),
                            ClarifyingQuestions.Command.INIT(it.itemOpportunity),
                            DoSubmitProposal.Command.DATA(it.itemOpportunity)
                    )
                    //AnchoredPanel.Command.Expand

                }
                is SubmitProposal.Result.ProposalRemoved -> {
                    //hide panel

                    Observable.empty<UiCommand>()

                }
                is FeesResult.CalculatorLoaded -> {
                    //ProposeTerms.Command.DATA
                    Observable.empty<UiCommand>()
                }
                is DoSubmitProposal.Result.Success -> {
                    println("SUCCESS")
                    Observable.just(SubmitProposal.Command.RemoveProposal)
                }

                else -> {
                    Observable.empty<UiCommand>()
                }
            /*is SuggestedRateResult.SuggestAccepted -> {
                ProposeTermsCommands.UpdateBid(it.suggestedRate)
            }
            is FeesResult.CalculatorLoaded -> {
                //and navigate to it
                ProposeTermsCommands.RecalculateBidEarn
            }
            is AnchorablePanelResult.Discard -> {
                //show dialog first
                //hide panel
                DoSubmitProposalCommand.Remove
            }


            is AnchorablePanelResult.PageChanged -> {
                //show dialog first
                DoSubmitProposalCommand.Expand
            }
            is DoSubmitProposalResult.Success -> {
                DoSubmitProposalCommand.Hide
            }
            is DoSubmitProposalResult.Error -> {
                DoSubmitProposalCommand.Hide
            }*/
            /*ProposalSummaryEvents.OnSubmitProposal -> {
                SubmitProposalCommand.DoSubmit(proposal)
            }

            ProposalSummaryEvents.ToCoverLetter -> {
                AnchorablePanelCommands.NavigateTo.SpecificPage(1)

            }
            ProposalSummaryEvents.ToPineappleQuestions -> {
                AnchorablePanelCommands.NavigateTo.SpecificPage(2)
            }
            ProposalSummaryEvents.ToProposeTerms -> {
                AnchorablePanelCommands.NavigateTo.SpecificPage(0)
            }*/
            }
        }
    }
}
