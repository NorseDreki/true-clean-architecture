package com.example.clean

import android.content.Context
import com.example.clean.screens.ProposalConfirmationScreen
import com.example.domain.*
import com.example.domain.submitProposal.ProposalConfirmation
import flow.Flow
import io.reactivex.Observable

class FlowNavigator(val context: Context) : Navigator {



    override fun <C : UiCommand, R : UiResult, S : UiState> display(component: UiComponent<C, R, S>, command: C) {

        val flow = Flow.get(context)

        component.process(Observable.just(command)).subscribe(
                { println("next $it") },
                { println("error $it") },
                { println("completed") }
        )


        val toScreen = ProposalConfirmationScreen.ToScreen(component as ProposalConfirmation)

        component.render().compose(toScreen)
                .doOnComplete {
                    println("I'm done here")
                    flow.goBack()
                }
                .subscribe {
                    flow.set(it)
                }
    }
}
