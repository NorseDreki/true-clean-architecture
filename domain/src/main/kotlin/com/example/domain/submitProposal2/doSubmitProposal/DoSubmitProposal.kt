package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.framework.Navigator
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.common.DialogState
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation

class DoSubmitProposal(
        val navigator: Navigator,
        val proposalConfirmation: ProposalConfirmation
): ExtraCommandsComponent<DoSubmitProposal.Command, DoSubmitProposal.Result, DoSubmitProposal.ViewState>() {
    override val processor = Processor(navigator, proposalConfirmation)
    override val reducer = Reducer()


    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command()
        data class ToggleSubmitEnabled(val enabled: Boolean) : Command()
        object DoSubmit : Command()
    }

    sealed class Result : UiResult {
        data class SubmitStatus(val enabled: Boolean) : Result()

        object InProgress : Result()
        data class Success(val response: String) : Result()
        data class Error(val exception: Throwable) : Result()
    }

    //error cases: already submitted, 500, 503

    data class ViewState(
            val isSubmitEnabled: Boolean,
            val status: DialogState
    ) : UiState {
        companion object {
            /*fun initial() = SubmitProposalViewState(
                    CoverLetterViewState("2312", false),
                    PineappleQuestionsViewState(listOf())
            )*/
        }
    }
}
//handle job updates to update job title in Proposal, and questions
//alert if both changed?

