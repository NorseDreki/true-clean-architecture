package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.CoverLetter2.*
import io.reactivex.ObservableTransformer


class CoverLetter2 : ClassicVersionComponent<Command, Result, ViewState>() {


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




    override val processor=
            ObservableTransformer<Command, Result> { t ->
                t//.debounce(3, TimeUnit.SECONDS)
                        .map {
                            when (it) {
                                is Command.DATA -> {
                                    if (it.itemOpportunity.itemDetails.isCoverLetterRequired) {
                                        result(it.itemOpportunity.proposal.coverLetter)
                                    } else {
                                        Result.NoCoverLetterRequired
                                    }
                                }
                                is Command.UpdateCoverLetter -> {
                                    result(it.coverLetter)
                                }
                            }
                        }

            }
    override val reducer=
            ObservableTransformer<Result, ViewState> {
                it.scan(ViewState())
                { state, result ->
                    when (result) {
                        is Result.Valid -> state.copy(result.coverLetter, false)
                        is Result.Empty -> state.copy("", false)
                        is Result.LengthExceeded -> state.copy(result.coverLetter, true)
                        else -> throw IllegalStateException("sdaf $state")
                    }
                }
            }

    private fun result(coverLetter: String): Result {
        //save cover letter to submit proposal storage?

        val validated = coverLetter.trim()

        val maxLength = 5000
        return when {
            validated.length > maxLength -> Result.LengthExceeded(validated, maxLength)
            validated.isNotEmpty() -> Result.Valid(validated)
            else -> Result.Empty
        }
    }
}
