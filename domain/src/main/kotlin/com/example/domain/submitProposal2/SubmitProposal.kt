package com.example.domain.submitProposal2


import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.framework.asEmbedded
import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import com.example.domain.submitProposal2.proposalSummary.ProposalSummaryViewState


class SubmitProposal(
        val coverLetter: CoverLetter,
        val clarifyingQuestions: ClarifyingQuestions,
        val doSubmitProposal: DoSubmitProposal
): ExtraCommandsComponent<SubmitProposal.Command, UiResult, SubmitProposal.ViewState>() {

    val cle = coverLetter.asEmbedded()
    val cqe = clarifyingQuestions.asEmbedded()
    val dsp = doSubmitProposal.asEmbedded()

    override val processor = Processor(cle.asActor, cqe.asActor, dsp.asActor)
    override val reducer = Reducer(cle.asReducer, cqe.asReducer, dsp.asReducer)

    sealed class Command : UiCommand {

        data class DATA(val itemDetails: ItemDetails) : Command()

        object ToNextStep : Command()
        object ToCoverLetter : Command()
        object ToClarifyingQuestions : Command()

        object RemoveProposal : Command() //when job became private
    }

    sealed class Result : UiResult {
        object ProposalRemoved : Result()
        object ProposalSent : Result() //change to already applied

        object ProposalUpdated : Result()
        data class ProposalLoaded(val itemOpportunity: ItemOpportunity) : Result() //time

        object JobIsPrivate : Result()
        object JobNoLongerAvailable : Result()


        data class NavigatedTo(val index: Int) : Result()

        object Dummy : Result()
    }

    data class ViewState(
            val coverLetter: CoverLetter.ViewState,
            val clarifyingQuestions: ClarifyingQuestions.ViewState,
            val proposalSummary: ProposalSummaryViewState,
            val doSubmitProposal: DoSubmitProposal.ViewState,
            val index: Int
    ) : UiState {
        companion object {
            /*fun initial() = SubmitProposalViewState(
                    CoverLetterViewState("2312", false),
                    PineappleQuestionsViewState(listOf())
            )*/
        }
    }
}
