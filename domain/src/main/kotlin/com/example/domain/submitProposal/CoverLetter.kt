package com.example.domain.submitProposal

import com.example.domain.*
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.CoverLetter.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class CoverLetter : UiComponent<Command, Result, ViewState> {
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


/*
class CoverLetter : UiComponent<> {

    val commandsSub = PublishSubject.create<UiCommand>()

    override fun processCommands(commands: Observable<UiCommand>) {
        commands.subscribe(commandsSub)
    }

    override fun produceResults(): Observable<UiResult> {
        return commandsSub.compose(coverLetterProcessor)
    }

    override fun render(): Observable<UiState> {
        return commandsSub.compose(coverLetterProcessor).compose(coverLetterReducer)
    }

    override fun asTransformer(): Observable.Transformer<UiResult, UiState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class CoverLetterEvents : UiEvent {
        data class CoverLetterUpdated(val coverLetter: String) : CoverLetterEvents()
    }

    sealed class CoverLetterCommand : UiCommand {
        //initial
        data class UpdateCoverLetter(val coverLetter: String) : CoverLetterCommand()
    }


    sealed class UpdateCoverLetterResult : UiResult {

        data class Valid(val coverLetter: String) : UpdateCoverLetterResult()
        object Empty : UpdateCoverLetterResult()

        data class LengthExceeded(val coverLetter: String, val errorMessage: String) : UpdateCoverLetterResult()
    }


    val toCLCommands =
            Observable.Transformer<UiEvent, UiCommand> {
                it.map {
                    when (it) {
                        is CoverLetterEvents.CoverLetterUpdated -> CoverLetterCommand.UpdateCoverLetter(it.coverLetter)
                        else -> {
                            throw IllegalStateException("sdaf")
                        }
                    }
                }
            }

    val coverLetterProcessor =
            Observable.Transformer<UiCommand, UiResult> { t ->
                t.debounce(3, TimeUnit.SECONDS)
                        .map {
                            when (it) {
                                is CoverLetterCommand.UpdateCoverLetter -> {

                                    //save cover letter to submit proposal storage?

                                    val validated = it.coverLetter.trim()


                                    if (validated.length > 5000) {
                                        UpdateCoverLetterResult.LengthExceeded(validated, "Too long for CL")
                                    } else if (validated.isNotEmpty()) {
                                        UpdateCoverLetterResult.Valid(validated)
                                    } else {
                                        UpdateCoverLetterResult.Empty
                                    }

                                }
                                else -> throw IllegalStateException("sdf")
                            }
                        }

            }

    val coverLetterReducer =
            Observable.Transformer<UiResult, UiState> {
                it.scan<UiState>(CoverLetterViewState())
                { state, result ->
                    when (result) {
                        is UpdateCoverLetterResult.Valid -> state.copy(result.coverLetter, false)
                        is UpdateCoverLetterResult.Empty -> state.copy("", false)
                        is UpdateCoverLetterResult.LengthExceeded -> state.copy(result.coverLetter, true)
                        else -> throw IllegalStateException("sdaf")
                    }
                }
            }




}*/
