package com.example.domain.submitProposal2.coverLetter

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal2.coverLetter.CoverLetter.*

class CoverLetter : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        data class UpdateCoverLetter(val coverLetter: String) : Command()
    }

    sealed class Result : UiResult {
        object NotRequired : Result()

        data class Valid(val coverLetter: String) : Result()
        data class LengthExceeded(val coverLetter: String, val limit: Int) : Result()
        object Empty : Result()
    }

    data class ViewState(
            val isVisible: Boolean = true,
            val coverLetter: String = "",
            val isLengthExceeded: Boolean = false
    ) : UiState


    override val processor = Processor()
    override val reducer = Reducer()
}
