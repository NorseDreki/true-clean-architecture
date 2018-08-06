package com.example.clean.screens2

import com.example.clean.R
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

data class ProposalConfirmationScreen(
        override val state: ProposalConfirmation.ViewState,
        override val events: ProposalConfirmationEvents
) : Screen {
    override val listBindings: ListBindings = object : ListBindings {}

    override val layout: Int
        get() = R.layout.proposal_confirmation_view

    class ToScreen(
            val proposalConfirmation: ProposalConfirmation
    ) : ObservableTransformer<ProposalConfirmation.ViewState, ProposalConfirmationScreen> {

        val events = ProposalConfirmationEvents(proposalConfirmation)

        override fun apply(
                upstream: Observable<ProposalConfirmation.ViewState>
        ): ObservableSource<ProposalConfirmationScreen> =

                upstream.map { ProposalConfirmationScreen(it, events) }
    }
}
