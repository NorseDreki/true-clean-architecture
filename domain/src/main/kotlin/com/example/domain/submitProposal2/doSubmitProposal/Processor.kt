package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.framework.Navigator
import com.example.domain.framework.withData
import com.example.domain.models.Proposal
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal.Command
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal.Result
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Processor(
        val navigator: Navigator,
        val proposalConfirmation: ProposalConfirmation
) : ObservableTransformer<Command, Result> {

    override fun apply(upstream: Observable<Command>) =
            upstream
                    .compose(withData)
                    .flatMap {
                        val command = it.currentCommand as Command
                        val proposal = (it.dataCommand as Command.DATA).itemOpportunity.proposal

                        when (command) {
                            is Command.ToggleSubmitEnabled ->
                                Observable.just(Result.SubmitEnabled(command.enabled))
                            Command.DoSubmit -> {
                                doSubmit(proposal)
                            }
                            is Command.DATA -> Observable.empty()
                        }
                    }!!

    private fun doSubmit(proposal: Proposal): Observable<Result> {
        val api: Api? = SomeApi()

        return api!!.submitProposal("123", "dsf")
                //.delay(3, TimeUnit.SECONDS)
                .map(Result::Success)
                .doOnNext {
                    navigator.display(
                            proposalConfirmation,
                            ProposalConfirmation.Command.DATA("some"))
                }
                .cast(Result::class.java)
                .onErrorReturn(Result::Error)
                .startWith(Result.InProgress)
    }
}
