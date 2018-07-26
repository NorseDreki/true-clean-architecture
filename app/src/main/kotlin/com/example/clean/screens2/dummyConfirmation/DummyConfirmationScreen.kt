package com.example.clean.screens2.dummyConfirmation

import com.example.clean.R
import com.example.clean.screens.ListBindings
import com.example.clean.screens.Screen
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.DummyConfirmation
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

data class DummyConfirmationScreen(
        override val state: DummyConfirmation.ViewState,
        override val events: DummyConfirmationEvents
) : Screen {
    override val listBindings: ListBindings = object : ListBindings {}

    override val layout: Int
        get() = R.layout.dummy_confirmation_view

    class ToScreen(
            val dummyConfirmation: DummyConfirmation
    ) : ObservableTransformer<DummyConfirmation.ViewState, DummyConfirmationScreen> {

        val events = DummyConfirmationEvents(dummyConfirmation)

        override fun apply(
                upstream: Observable<DummyConfirmation.ViewState>
        ): ObservableSource<DummyConfirmationScreen> =

                upstream.map { DummyConfirmationScreen(it, events) }
    }
}
