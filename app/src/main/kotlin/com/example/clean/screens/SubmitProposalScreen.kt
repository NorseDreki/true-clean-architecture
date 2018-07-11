package com.example.clean.screens

import com.example.clean.BR
import com.example.clean.R
import com.example.clean.screens.SubmitProposalScreen.Events
import com.example.domain.submitProposal.SubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import me.tatarka.bindingcollectionadapter2.ItemBinding

data class SubmitProposalScreen(
        override val state: SubmitProposal.ViewState,
        override val events: Events,
        override val listBindings: ListBindings
) : Screen {

    override val layout: Int
        get() = R.layout.submit_proposal_view_content


    data class Bindings(
            val clarifyingQuestions: ClarifyingQuestions,
            val proposalSummary: ProposalSummary
    ) : ListBindings {

        class ClarifyingQuestions(
                val onItemClickListener: OnItemClickListener
        ) {
            val questionBinding = ItemBinding.of<com.example.domain.submitProposal.ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.clarifying_question_item)
                    .bindExtra(BR.listener, onItemClickListener)
        }

        class ProposalSummary() {
            val questionBinding: ItemBinding<com.example.domain.submitProposal.ClarifyingQuestions.QuestionViewState> = ItemBinding.of<com.example.domain.submitProposal.ClarifyingQuestions.QuestionViewState>(BR.v, R.layout.answered_question_item)
        }
    }

    data class Events(
            val coverLetter: CoverLetterScreenEvents,
            val clarifyingQuestions: ClarifyingQuestionsEvents,
            val proposalSummaryEvents: ProposalSummaryEventHandler,
            val doSubmitProposal: DoSubmitProposalEvents
    ) : ScreenEvents
}

class ToScreen(
        val submitProposal: SubmitProposal
) : ObservableTransformer<SubmitProposal.ViewState, SubmitProposalScreen> {

    val clarifyingQuestions = ClarifyingQuestionsEvents(
            submitProposal.clarifyingQuestions
    )
    val events = Events(
            CoverLetterScreenEvents(
                    submitProposal.coverLetter
            ),
            clarifyingQuestions,
            SubmitProposalEvents(submitProposal),
            DoSubmitProposalEvents(
                    submitProposal.doSubmitProposal
            )
    )

    val bindings = SubmitProposalScreen.Bindings(
            SubmitProposalScreen.Bindings.ClarifyingQuestions(clarifyingQuestions),
            SubmitProposalScreen.Bindings.ProposalSummary()
    )

    override fun apply(
            upstream: Observable<SubmitProposal.ViewState>
    ): ObservableSource<SubmitProposalScreen> =

            Observable.combineLatest(
                    upstream,
                    Observable.just(events),
                    BiFunction { left, right ->
                        SubmitProposalScreen(left, right, bindings)
                    }
            )
}
