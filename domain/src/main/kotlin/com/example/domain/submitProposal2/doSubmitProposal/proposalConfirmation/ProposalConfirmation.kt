package com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import io.reactivex.ObservableTransformer

class ProposalConfirmation: ExtraCommandsComponent<ProposalConfirmation.Command, ProposalConfirmation.Result, ProposalConfirmation.ViewState>() {
    val pcProcessor =
            ObservableTransformer<Command, Result> {
                it.map {
                    when (it) {
                        is Command.DATA -> Result.DATALoaded(it.itemOpportunity)
                        Command.Dismiss -> Result.Dismissed
                    }
                }
            }

    val pcReducer =
            ObservableTransformer<Result, ViewState> {
                it.map { ViewState((it as Result.DATALoaded).itemOpportunity) }
            }

    override val processor = pcProcessor
    override val reducer = pcReducer


    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: String) : Command()

        object Dismiss : Command()
    }

    sealed class Result : UiResult {
        data class DATALoaded(val itemOpportunity: String) : Result()

        object Dismissed : Result()
    }

    data class ViewState(
            val title: String
    ) : UiState
}
