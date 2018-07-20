package com.example.domain.submitProposal2

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.framework.asEmbedded
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.fees.FeesResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.ReplaySubject

class Processor(
        val coverLetter: CoverLetter,
        val clarifyingQuestions: ClarifyingQuestions,
        val doSubmitProposal: DoSubmitProposal
) : ObservableTransformer<SubmitProposal.Command, SubmitProposal.Result> {

    val loopbackCommands = ReplaySubject.create<UiCommand>()

    override fun apply(upstream: Observable<SubmitProposal.Command>): ObservableSource<SubmitProposal.Result> {
        val c= upstream
                .cast(UiCommand::class.java)
                .mergeWith(loopbackCommands.doOnNext { println("GOT LB $it") })
                .doOnNext { println("RESSP " + it) }
                .publish<UiResult> { shared ->
                    Observable.merge<UiResult>(
                            Observable.just(

                                    shared.ofType(SubmitProposal.Command::class.java)
                                            .compose(storageLoader),
                                    shared.ofType(SubmitProposal.Command.ToNextStep::class.java)
                                            .compose(navigationProcessor),

                                    shared.ofType(CoverLetter.Command::class.java)
                                            .compose(coverLetter.asEmbedded().asActor),
                                    shared.ofType(ClarifyingQuestions.Command::class.java)
                                            .compose(clarifyingQuestions.asEmbedded().asActor),

                                    shared.ofType(DoSubmitProposal.Command::class.java)
                                            .compose(doSubmitProposal.asEmbedded().asActor)

                            )
                            /*,


                            shared.ofType(FeesCommands::class.java)
                                    .compose(feesProc)*/
                    )
                }
                .publish { shared ->
                    Observable.merge(
                            shared,
                            shared.compose(submitAllowedProcessor),//.doOnNext { println(">>>> SAP: $it") },
                            shared.compose(storageSaver)
                    )
                }
                .publish { shared ->
                    Observable.merge(
                            shared,//.doOnNext { println(">>>> SHARED AFTER SAP: $it") },
                            shared.compose(submitProposalResultsProcessor)
                    )
                }//.doOnNext { println(">>>> BEFORE SHARE: $it") }
                //.startWith(Result.Dummy)
                //.share()
                //.publish().autoConnect(2).doOnNext { println(">>>> AFTER SHARE: $it") }
                .replay().refCount().doOnNext { println(">>>> AFTER SHARE: $it") }


        //properly unsubscribe?
        c.compose(fromResultToCommands)
                //.takeWhile
                .doOnNext { println("GOT COMMAND: $it") }
                //.delay(10, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("GOT COMMAND SUB $it")
                    loopbackCommands.onNext(it)
                }

        return c.filter { it is SubmitProposal.Result }.cast(SubmitProposal.Result::class.java)
    }


    val fromResultToCommands =
            ObservableTransformer<UiResult, UiCommand> {
                it.flatMap {

                    //memoize itemOpportunity here as well?
                    when (it) {
                        is SubmitProposal.Result.ProposalUpdated -> {
                            println("112233 PROPOSAL UPDATED")
                            Observable.empty<UiCommand>()
                        }
                        is SubmitAllowedResult.Enabled -> {
                            //Observable.just(ProposalSummary.Command.ToggleSubmitEnabled(true))
                            println("SUBMIT ENABLED!")

                            Observable.just(DoSubmitProposal.Command.ToggleSubmitEnabled(true))
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

    val submitProposalResultsProcessor =
            ObservableTransformer<UiResult, SubmitProposal.Result> {
                it.flatMap {
                    when (it) {
                        CoverLetter.Result.Empty,
                        is CoverLetter.Result.Valid,
                        is ClarifyingQuestions.Result.ValidAnswer,
                        is ClarifyingQuestions.Result.EmptyAnswer
                        /*is ProposeTerms.Result.BidValid,
                        ProposeTerms.Result.BidEmpty*/ ->//,
                            //is ProposeTerms.Result.EngagementSelected ->

                            Observable.just(SubmitProposal.Result.ProposalUpdated)

                        is DoSubmitProposal.Result.Success ->
                            Observable.just(SubmitProposal.Result.ProposalSent)

                        is DoSubmitProposal.Result.Error ->
                            Observable.just(SubmitProposal.Result.JobNoLongerAvailable)

                        else -> Observable.empty()
                    }
                }
            }

}