package com.example.domain.submitProposal2.doSubmitProposal

import com.example.domain.DataCommand
import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.framework.Navigator
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.common.DialogState
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal.*
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation

class DoSubmitProposal(
        navigator: Navigator,
        val proposalConfirmation: ProposalConfirmation
): ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), DataCommand

        data class ToggleSubmitEnabled(val enabled: Boolean) : Command()
        object DoSubmit : Command()
    }

    sealed class Result : UiResult {
        data class SubmitEnabled(val enabled: Boolean) : Result()

        object InProgress : Result()
        data class Success(val response: String) : Result()
        data class Error(val exception: Throwable) : Result()
    }

    //error cases: already submitted, 500, 503

    data class ViewState(
            val isSubmitEnabled: Boolean = false,
            val status: DialogState = DialogState.Dismissed
    ) : UiState

    override val processor = Processor(navigator, proposalConfirmation)
    override val reducer = Reducer()
}
//handle job updates to update job title in Proposal, and questions
//alert if both changed?
