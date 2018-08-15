package com.example.domain.submitProposal2.storage

import com.example.domain.Processor
import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
import com.example.domain.submitProposal2.SubmitProposal
import io.reactivex.Observable

class StorageLoaderProcessor : Processor<SubmitProposal.Command, SubmitProposal.Result> {

    override fun apply(upstream: Observable<SubmitProposal.Command>) =
            upstream.flatMap {
                when (it) {
                    is SubmitProposal.Command.DATA -> {
                        //or move this to "from results to commands"
                        val itemOpportunity =
                                createItemOpportunity((it).itemDetails)

                        //or created
                        Observable.just(SubmitProposal.Result.ProposalLoaded(itemOpportunity))
                    }
                    is SubmitProposal.Command.RemoveProposal -> {
//                        repo!!.removeProposal()

                        Observable.just(SubmitProposal.Result.ProposalRemoved)
                    }
                    else -> Observable.empty()
                }
            }

    private fun createItemOpportunity(itemDetails: ItemDetails): ItemOpportunity {
        val questions = listOf(
                Question("1", "q1"),
                Question("2", "q1")
        )

        val withQuestions = ItemOpportunity(
                ItemDetails("1234", false, questions),
                Proposal(0, null, "")
        )

        return ItemOpportunity(
                itemDetails,
                Proposal(0, null, "123423423")
        )

    }
}
