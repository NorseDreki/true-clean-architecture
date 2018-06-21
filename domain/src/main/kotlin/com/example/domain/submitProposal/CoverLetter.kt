package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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


    data class CoverLetterViewState(
            val coverLetter: String = "",
            val isLengthExceeded: Boolean = false
    ) : UiState


}*/
