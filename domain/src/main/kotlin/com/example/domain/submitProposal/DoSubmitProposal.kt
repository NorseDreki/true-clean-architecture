package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.models.Proposal
import com.example.domain.submitProposal.DoSubmitProposal.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class DoSubmitProposal : UiComponent<Command, Result, ViewState> {

    val cmd = PublishSubject.create<Command>()

    fun fromEvent(command: Command) {
        cmd.onNext(command)
    }

    lateinit var results: Observable<Result>

    override fun process(commands: Observable<Command>): Observable<Result> {
        val cast = commands
                .mergeWith(cmd.doOnNext { println("DSP MERGEd CMD") })
                .doOnNext { println("CMDDSP " + it) }
                .compose(dspProcessor)
                .doOnNext { println("RESDSP " + it) }
                .cast(Result::class.java)
                .replay()
                .refCount()

        results = cast

        return cast
    }

    override fun render(): Observable<ViewState> {
        return results.compose<ViewState>(dspReducer)//.distinctUntilChanged()
    }

    val dspReducer =
            ObservableTransformer<Result, ViewState> {
                it.scan(ViewState(false, DialogState.Dismissed)) { state, result ->
                    when (result) {
                        is Result.SubmitStatus -> state.copy(isSubmitEnabled = result.enabled)
                        Result.InProgress -> state.copy(
                                status = DialogState.Progress(null, "In Progress")
                        )
                        is Result.Success -> state.copy(
                                status = DialogState.Dismissed
                        )
                        is Result.Error -> state.copy(
                                status = DialogState.Alert(
                                        "title",
                                        "message",
                                        "Retry",
                                        "Cancel"
                                )
                        )
                    }
                }
            }

    val dspProcessor =
            ObservableTransformer<Command, Result> {
                it.publish { shared ->
                    println("DSP COMMAND $shared")
                    Observable.combineLatest<Proposal, Command, Pair<Proposal, Command>>(
                            shared.ofType(Command.DATA::class.java).map { it.proposal },
                            shared.filter { it !is Command.DATA },

                            BiFunction { proposal, command ->
                                Pair(proposal, command)
                            }
                    ).flatMap {
                        val command = it.second
                        val proposal = it.first
                        when (command) {
                            is Command.ToggleSubmitEnabled ->
                                Observable.just(Result.SubmitStatus(command.enabled))
                            is Command.DoSubmit -> {
                                doSubmit(proposal)
                            }
                            is Command.DATA -> throw IllegalStateException("dsf")
                        }
                    }
                }
            }

    private fun doSubmit(proposal: Proposal): Observable<Result> {
        val api: Api? = SomeApi()

        return api!!.submitProposal("123", "dsf")
                //.delay(3, TimeUnit.SECONDS)
                .map(Result::Success)
                .cast(Result::class.java)
                .onErrorReturn(Result::Error)
                .startWith(Result.InProgress)
    }

    sealed class Command : UiCommand {
        data class DATA(val proposal: Proposal) : Command()
        data class ToggleSubmitEnabled(val enabled: Boolean) : Command()
        object DoSubmit : Command()
    }

    sealed class Result : UiResult {
        data class SubmitStatus(val enabled: Boolean) : Result()

        object InProgress : Result()
        data class Success(val response: String) : Result()
        data class Error(val exception: Throwable) : Result()
    }

    //error cases: already submitted, 500, 503

    data class ViewState(
            val isSubmitEnabled: Boolean,
            val status: DialogState
    ) : UiState {
        companion object {
            /*fun initial() = SubmitProposalViewState(
                    CoverLetterViewState("2312", false),
                    PineappleQuestionsViewState(listOf())
            )*/
        }
    }
}

//handle job updates to update job title in Proposal, and questions
//alert if both changed?

interface Api {
    fun submitProposal(id: String, some: String): Observable<String>
}

class SomeApi : Api {
    override fun submitProposal(id: String, some: String): Observable<String> {
        //return Observable.just("response")
        return Observable.error(IllegalStateException("sf"))
    }

}
