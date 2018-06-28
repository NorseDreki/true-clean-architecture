package com.example.domain.submitProposal

import com.example.domain.UiResult
import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
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
                                Observable.just(SubmitProposal.Result.ProposalUpdated(itemOpportunity))
                    }
                    is SubmitProposal.Command.RemoveProposal -> {
                        repo!!.removeProposal()

                        Observable.just(SubmitProposal.Result.ProposalRemoved)
                    }
                }
            }
        }

var repo: ProposalRepository? = null

val storageSaver =
        ObservableTransformer<UiResult, UiResult> {
            it.flatMap {
                when(it) {
                    is ClarifyingQuestions.Result.ValidAnswer -> {
                        try {
                            repo!!.updateCoverLetter("234", "324")
                            //Observable.just(SubmitProposalStorageResult.Success)
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
            Proposal(0, null, "")
    )
}
/*

class SubmitProposalStorageProcessor(val storage: SubmitProposalStorage) : Observable.Transformer<UiCommand, UiResult> {
    override fun call(t: Observable<UiCommand>): Observable<UiResult> {
        */
/*return t.publish<SubmitProposalStorageResult> { shared ->
            Observable.merge(
                shared.ofType(CoverLetterCommand::class.java).map {

                    SubmitProposalStorageResult.Success
                },
                shared.ofType(PineappleQuestionCommand::class.java).map {

                    SubmitProposalStorageResult.Success
                }
            )
        }*//*


        return t.map {
            when (it) {
                is SubmitProposal.CoverLetterCommand.UpdateCoverLetter -> {

                    val jobId = "1" ///??????

                    updateCoverLetter(jobId, it.coverLetter)

                    SubmitProposal.SubmitProposalStorageResult.Success
                }
                is SubmitProposal.PineappleQuestionCommand.UpdatePineappleQuestionAnswer -> {

                    val jobId = "1" ///??????

                    updateAnswer(jobId, it.question, it.answer)


                    SubmitProposal.SubmitProposalStorageResult.Success
                }

                else -> throw IllegalStateException("sdaf")
            }
        }
    }

    fun updateCoverLetter(jobId: String, coverLetter: String) {

        val proposal = storage.getProposal(null, jobId)
        val needsUpdating = !Utils.equals(proposal.getCoverLetter(), coverLetter)

        if (needsUpdating) {
            proposal.setCoverLetter(coverLetter)
            proposal.setUpdated(System.currentTimeMillis())
        }

        val isUpdated = needsUpdating

        if (isUpdated) {
            storage.notifyProposalUpdated(jobId)
        }
    }

    fun updateAnswer(jobId: String, question: String, answer: String) {
        val proposal = storage.getProposal(null, jobId)

        var answeredQuestion: AnsweredQuestion? = proposal.getQuestions().where()
                .equalTo("question", question)
                .findFirst()

        if (answeredQuestion == null) {
            //answeredQuestion = realm.createObject(AnsweredQuestion::class.java)
            answeredQuestion!!.question = question

            proposal.getQuestions().add(answeredQuestion)
        }

        val needsUpdating = !Utils.equals(answeredQuestion.answer, answer)

        if (needsUpdating) {
            answeredQuestion.answer = answer
            proposal.setUpdated(System.currentTimeMillis())
        }

        val isUpdated = needsUpdating

        if (isUpdated) {
            storage.notifyProposalUpdated(jobId)
        }
    }


}*/
