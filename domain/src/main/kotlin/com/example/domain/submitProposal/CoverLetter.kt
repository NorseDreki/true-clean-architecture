package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.CoverLetter.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class CoverLetter : UiComponent<Command, Result, ViewState> {

    val cmd = PublishSubject.create<Command>()

    fun fromEvent(command: Command) {
        cmd.onNext(command)
    }


    lateinit var results: Observable<Result>

    override fun process(commands: Observable<Command>): Observable<Result> {
        val cast = commands
                .mergeWith(cmd)
                .doOnNext { println("CMDCL " + it) }
                .compose(coverLetterProcessor)
                .doOnNext { println("RESCL " + it) }
                .cast(Result::class.java)
                .replay()
                .refCount()

        results = cast

        return cast
    }

    override fun render(): Observable<ViewState> {
        return results.compose<ViewState>(coverLetterReducer)
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

    val coverLetterReducer =
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

}
