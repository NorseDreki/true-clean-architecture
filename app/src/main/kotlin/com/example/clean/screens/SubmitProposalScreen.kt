package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.R
import com.example.clean.screens.SubmitProposalScreen.Events
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.SubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import me.tatarka.bindingcollectionadapter2.ItemBinding

data class SubmitProposalScreen(
        override val state: SubmitProposal.ViewState,
        override val events: Events
) : Screen {

    override val layout: Int
        get() = R.layout.submit_proposal_view_content

    data class Events(
            val coverLetter: CoverLetterScreenEvents,
            val clarifyingQuestions: ClarifyingQuestionsEvents,
            val proposalSummaryEvents: ProposalSummaryEventHandler,
            val doSubmitProposal: DoSubmitProposalEvents,
            val itemBinding: ItemBinding<ClarifyingQuestions.QuestionViewState> = ItemBinding.of<ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.answered_question_item)
    ) : ScreenEvents
}

class ToScreen(
        val submitProposal: SubmitProposal
) : ObservableTransformer<SubmitProposal.ViewState, SubmitProposalScreen> {

    val events = Events(
            CoverLetterScreenEvents(
                    submitProposal.coverLetter
            ),
            ClarifyingQuestionsEvents(
                    submitProposal.clarifyingQuestions
            ),
            /*ProposalSummaryEvents(
                    submitProposal,
                    DialogEvents(
                            { println("positive");
                                submitProposal.handleProposalSummaryEvent(ProposalSummaryEventHandler.Event.OnSubmit)
                            },
                            { println("negative") }
                    )
            ),*/
            SubmitProposalEvents(submitProposal),
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
