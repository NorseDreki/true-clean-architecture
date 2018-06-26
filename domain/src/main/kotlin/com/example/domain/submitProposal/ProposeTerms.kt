package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.CoverLetter.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class ProposeTerms : UiComponent<Command, Result, ViewState> {
    override fun process(commands: Observable<Command>): Observable<Result> {
        return commands
                .doOnNext { println("CMD " + it) }
                .compose(coverLetterProcessor)
                .doOnNext { println("RES " + it) }
                .cast(Result::class.java)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val coverLetterProcessor =
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


    sealed class CoverLetterEvents : UiEvent {
        data class CoverLetterUpdated(val coverLetter: String) : CoverLetterEvents()
    }

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
            val coverLetter: String = "",
            val isLengthExceeded: Boolean = false
    ) : UiState

}

