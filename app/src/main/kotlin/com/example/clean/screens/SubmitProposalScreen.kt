package com.example.clean.screens

import com.example.clean.R
import com.example.domain.submitProposal.SubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

data class SubmitProposalScreen(
        override val state: SubmitProposal.ViewState,
        override val events: SubmitProposalScreenEvents
) : Screen {

    override val layout: Int
        get() = R.layout.submit_proposal_view_content

    companion object {
        var screen: SubmitProposalScreen? = null
        lateinit var events: SubmitProposalScreenEvents

        fun fromState(submitProposal: SubmitProposal, state: SubmitProposal.ViewState): SubmitProposalScreen {
            if (screen == null) {

                events = SubmitProposalScreenEvents(
                        CoverLetterScreenEvents(
                                submitProposal.coverLetter
                        ),
                        ClarifyingQuestionsEvents(
                                submitProposal.clarifyingQuestions
                        )
                )
                screen = SubmitProposalScreen(state, events)

            } else {
                screen = screen!!.copy(state)
            }

            return screen!!
        }

    }
}

class ToScreen(val submitProposal: SubmitProposal) :
        ObservableTransformer<SubmitProposal.ViewState, SubmitProposalScreen> {

    val events = SubmitProposalScreenEvents(
            CoverLetterScreenEvents(
                    submitProposal.coverLetter
            ),
            ClarifyingQuestionsEvents(
                    submitProposal.clarifyingQuestions
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
