package com.example.clean

import android.content.Context
import com.example.clean.screens.Screen
import com.example.clean.screens2.ProposalConfirmationScreen
import com.example.clean.screens2.dummyConfirmation.DummyConfirmationScreen
import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.Component
import com.example.domain.framework.Navigator
import com.example.domain.framework.asStandalone
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.DummyConfirmation
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import flow.Flow
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class FlowNavigator2(
        val context: Context,
        val changer: Changer) : Navigator {

    override fun <C : UiCommand, R : UiResult, S : UiState> display(component: Component<C, R, S>, command: C) {
        val flow = Flow.get(context)


        when (component) {
            is ProposalConfirmation -> {
                val toScreen =
                        ProposalConfirmation.ViewState::class.java to ProposalConfirmationScreen.ToScreen(component as ProposalConfirmation)

                //component.render().compose(toScreen)
                val ps = component as ProposalConfirmation
                val asStandalone = ps.asStandalone(command as ProposalConfirmation.Command)

                println("FLOW NAV 2")
                changer.setTop(asStandalone.viewStates() as Observable<UiState>, toScreen as Pair<Class<UiState>, ObservableTransformer<UiState, Screen>>)

            }
            is DummyConfirmation -> {
                val toScreen =
                        DummyConfirmation.ViewState::class.java to DummyConfirmationScreen.ToScreen(component)

                //component.render().compose(toScreen)
                val ps = component as DummyConfirmation
                val asStandalone = ps.asStandalone(command as DummyConfirmation.Command)

                println("FLOW NAV 2")
                changer.setTop(asStandalone.viewStates() as Observable<UiState>, toScreen as Pair<Class<UiState>, ObservableTransformer<UiState, Screen>>)

            }
        }


        /*asStandalone.viewStates().compose(toScreen)
                .doOnComplete {
                    println("I'm done here")
                    flow.goBack()
                }
                .observeOn(AndroidSchedulers.mainThread())
                //.observeOn(Andr)
                .subscribe {
                    println("flow nav 2 set")
                    flow.set(it)
                }*/
    }
}
