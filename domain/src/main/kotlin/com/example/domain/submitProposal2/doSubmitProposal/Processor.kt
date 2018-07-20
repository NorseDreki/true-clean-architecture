package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.framework.Navigator
import com.example.domain.models.Proposal
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class Processor(
        val navigator: Navigator,
        val proposalConfirmation: ProposalConfirmation
) : ObservableTransformer<DoSubmitProposal.Command, DoSubmitProposal.Result> {

    override fun apply(upstream: Observable<DoSubmitProposal.Command>): Observable<DoSubmitProposal.Result> {
        return upstream.publish { shared ->
            println("DSP COMMAND $shared")
            Observable.combineLatest<Proposal, DoSubmitProposal.Command, Pair<Proposal, DoSubmitProposal.Command>>(

                    shared.ofType(DoSubmitProposal.Command.DATA::class.java).map {
                        /*navigator.display(
                                proposalConfirmation,
                                ProposalConfirmation.Command.DATA(it.itemOpportunity)
                        )*/
                        it.itemOpportunity.proposal

                    },
                    shared.filter { it !is DoSubmitProposal.Command.DATA },

                    BiFunction { proposal, command ->
                        Pair(proposal, command)
                    }
            ).flatMap {
                val command = it.second
                val proposal = it.first
                when (command) {
                    is DoSubmitProposal.Command.ToggleSubmitEnabled ->
                        Observable.just(DoSubmitProposal.Result.SubmitStatus(command.enabled))
                    is DoSubmitProposal.Command.DoSubmit -> {
                        doSubmit(proposal)
                    }
                    is DoSubmitProposal.Command.DATA -> throw IllegalStateException("dsf")
                }
            }.cast(DoSubmitProposal.Result::class.java)
        }
    }

    private fun doSubmit(proposal: Proposal): Observable<DoSubmitProposal.Result> {

        println("DO SUBMIT")
        val api: Api? = SomeApi()

        return api!!.submitProposal("123", "dsf")
                //.delay(3, TimeUnit.SECONDS)
                .map(DoSubmitProposal.Result::Success)
                .doOnNext {
                    navigator.display(
                            proposalConfirmation,
                            ProposalConfirmation.Command.DATA("some"))
                }
                .cast(DoSubmitProposal.Result::class.java)
                .onErrorReturn(DoSubmitProposal.Result::Error)
                .startWith(DoSubmitProposal.Result.InProgress)
    }


}
