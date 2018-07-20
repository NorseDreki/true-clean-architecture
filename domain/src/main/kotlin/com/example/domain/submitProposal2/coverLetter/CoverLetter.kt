package com.example.domain.submitProposal2.coverLetter

import com.example.domain.UiCommand
import com.example.domain.UiDataCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity


class CoverLetter : ExtraCommandsComponent<CoverLetter.Command, CoverLetter.Result, CoverLetter.ViewState>() {
    override val processor = Processor()
    override val reducer = Reducer()


    sealed class Command : UiCommand {
        data class DATA(val itemOpportunity: ItemOpportunity) : Command(), UiDataCommand

        data class UpdateCoverLetter(val coverLetter: String) : Command()
    }

    sealed class Result : UiResult {

        object NoCoverLetterRequired : Result()

        sealed class Status: Result() {
            object Completed: Status()
            object NotCompleted : Status()
        }

        data class Valid(val coverLetter: String) : Result()
        object Empty : Result()
        data class LengthExceeded(val coverLetter: String, val limit: Int) : Result()
    }

    data class ViewState(
            val coverLetter: String = "default",
            val isLengthExceeded: Boolean = false
    ) : UiState
}
