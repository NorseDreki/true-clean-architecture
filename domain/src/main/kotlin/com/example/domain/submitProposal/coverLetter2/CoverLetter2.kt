package com.example.domain.submitProposal.coverLetter2

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.coverLetter2.CoverLetter2.*


class CoverLetter2 : ClassicVersionComponent<Command, Result, ViewState>() {
    override val processor = CoverLetterProcessor()
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
