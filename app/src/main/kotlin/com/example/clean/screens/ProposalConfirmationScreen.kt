package com.example.clean.screens

import com.example.clean.R
import com.example.domain.submitProposal.DialogEvents
import com.example.domain.submitProposal.ProposalSummaryEventHandler
import com.example.domain.submitProposal.SubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

data class ProposalConfirmationScreen(
        override val state: SubmitProposal.ViewState,
        override val events: Events
) : Screen {

    override val layout: Int
        get() = R.layout.proposal_confirmation_view

    data class Events(
            val coverLetter: CoverLetterScreenEvents,
            val clarifyingQuestions: ClarifyingQuestionsEvents,
            val proposalSummaryEvents: ProposalSummaryEvents,
            val doSubmitProposal: DoSubmitProposalEvents
    ) : ScreenEvents

    class ToScreen(
            val submitProposal: SubmitProposal
    ) : ObservableTransformer<SubmitProposal.ViewState, SubmitProposalScreen> {

        val events = SubmitProposalScreen.Events(
                CoverLetterScreenEvents(
                        submitProposal.coverLetter
                ),
                ClarifyingQuestionsEvents(
                        submitProposal.clarifyingQuestions
                ),
                ProposalSummaryEvents(
                        submitProposal,
                        DialogEvents(
                                {
                                    println("positive");
                                    submitProposal.handleProposalSummaryEvent(ProposalSummaryEventHandler.Event.OnSubmit)
                                },
                                { println("negative") }
                        )
                ),
                DoSubmitProposalEvents(
                        submitProposal.doSubmitProposal
                )
        )

        override fun apply(
                upstream: Observable<SubmitProposal.ViewState>
        ): ObservableSource<SubmitProposalScreen> =

                Observable.combineLatest(
                        upstream,
                        Observable.just(events),
                        BiFunction { left, right ->
                            SubmitProposalScreen(left, right)
                        }
                )
    }
}
