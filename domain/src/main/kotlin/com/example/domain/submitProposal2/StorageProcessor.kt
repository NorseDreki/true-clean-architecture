package com.example.domain.submitProposal2

import com.example.domain.UiResult
import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

val storageLoader =
        ObservableTransformer<SubmitProposal.Command, SubmitProposal.Result> {
            it.flatMap {
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
        }

var repo: ProposalRepository? = null

val storageSaver =
        ObservableTransformer<UiResult, UiResult> {
            it.flatMap {
                when(it) {
                    is ClarifyingQuestions.Result.AnswerValid -> {
                        try {
//                            repo!!.updateCoverLetter("234", "324")
                            Observable.empty<UiResult>()
                        } catch (e: Exception) {
                            Observable.error<UiResult>(e)
                        }
                    }
                    else -> Observable.empty()
                }
            }
        }

interface ProposalRepository {
    fun updateCoverLetter(id: String, coverLetter: String)

    fun updateQuestionAnswer()

    fun removeProposal()
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
