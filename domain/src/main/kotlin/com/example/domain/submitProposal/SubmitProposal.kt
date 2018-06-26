package com.example.domain.submitProposal


import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.models.ItemDetails
import com.example.domain.submitProposal.SubmitProposal.Command
import com.example.domain.submitProposal.SubmitProposal.ViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject


class SubmitProposal(
        val coverLetter: CoverLetter,
        val clarifyingQuestions: ClarifyingQuestions
) : UiComponent<Command, UiResult, ViewState> {


    val loopbackCommands = PublishSubject.create<UiCommand>()

    override fun process(commands: Observable<Command>): Observable<UiResult> {
        val c = commands
                .doOnNext { println("CMD " + it) }
                .compose(storageProcessor)
                //.takeUntil
                .mergeWith(loopbackCommands)
                .doOnNext { println("RES " + it) }
                .publish { shared ->
                    Observable.merge<UiResult>(
                            shared.ofType(CoverLetter.Command::class.java)
                                    .compose { coverLetter.process(it) },
                            shared.ofType(ClarifyingQuestions.Command::class.java)
                                    .compose { clarifyingQuestions.process(it) }
                    )
                }
                .share()

        //properly unsubscribe?
        /*c.compose(fromResultToCommands)
                //.takeWhile
                .subscribe(loopbackCommands)*/

        return c
               // .cast(Result::class.java)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class Command : UiCommand {

        data class DATA(val itemDetails: ItemDetails) : Command()

        object RemoveProposal : Command() //when job became private
    }

    val fromResultToCommands =
            ObservableTransformer<UiResult, UiCommand> {
                it.map {
                    when (it) {
                        else -> throw IllegalStateException("sdaf")
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


    sealed class Result : UiResult {
        //object ProposalRemoved : Result()
        object ProposalSent : Result() //change to already applied
        //object ProposalCreated : Result()
        object ProposalUpdated : Result() //time
        object JobIsPrivate : Result()
        object JobNoLongerAvailable : Result()
    }
    data class ViewState(
            val coverLetterViewState: CoverLetter.ViewState,
            val pineappleQuestionsViewState: ClarifyingQuestions.ViewState
    ) : UiState {
        companion object {
            /*fun initial() = SubmitProposalViewState(
                    CoverLetterViewState("2312", false),
                    PineappleQuestionsViewState(listOf())
            )*/
        }
    }
}





