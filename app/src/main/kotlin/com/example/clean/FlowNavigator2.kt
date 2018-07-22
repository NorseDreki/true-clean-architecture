package com.example.clean

import android.content.Context
import com.example.clean.screens2.ProposalConfirmationScreen
import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.Component
import com.example.domain.framework.Navigator
import com.example.domain.framework.asStandalone
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import flow.Flow
import io.reactivex.android.schedulers.AndroidSchedulers

class FlowNavigator2(val context: Context) : Navigator {

    override fun <C : UiCommand, R : UiResult, S : UiState> display(component: Component<C, R, S>, command: C) {
        val flow = Flow.get(context)

        val toScreen =
                ProposalConfirmationScreen.ToScreen(component as ProposalConfirmation)

        //component.render().compose(toScreen)
        val ps = component as ProposalConfirmation
        ps.asStandalone(command as ProposalConfirmation.Command).viewStates().compose(toScreen)
                .doOnComplete {
                    println("I'm done here")
                    flow.goBack()
                }
                .observeOn(AndroidSchedulers.mainThread())
                //.observeOn(Andr)
                .subscribe {
                    flow.set(it)
                }
    }
}
