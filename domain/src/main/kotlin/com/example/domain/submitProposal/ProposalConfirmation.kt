package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class ProposalConfirmation : UiComponent<ProposalConfirmation.Command, ProposalConfirmation.Result, ProposalConfirmation.ViewState> {

    val cmd = PublishSubject.create<Command>()

    fun fromEvent(command: Command) {
        cmd.onNext(command)
    }

    lateinit var results: Observable<Result>

    override fun process(commands: Observable<Command>): Observable<Result> {
        val cast = commands
                .mergeWith(cmd)
                .compose(pcProcessor)
                .cast(Result::class.java)
                .replay()
                .refCount()

        results = cast

        return cast
    }

    override fun render(): Observable<ViewState> {
        return results
                .takeUntil(results.filter { it == Result.Dismissed })
                .compose<ViewState>(pcReducer)//.distinctUntilChanged()
    }
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
}
