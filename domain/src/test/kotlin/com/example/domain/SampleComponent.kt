package com.example.domain

import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class SampleComponent : com.example.domain.framework.Component<SampleComponent.Command, SampleComponent.Result, SampleComponent.ViewState> {
    override val commands = PublishSubject.create<Command>()

    override val processor = ObservableTransformer<Command, Result> {
        it.map { println("processing"); Result.Empty }
    }
    override val reducer = ObservableTransformer<Result, ViewState> {
        it.map { println("mapping");ViewState() }
    }


    sealed class Command : UiCommand {
        data class DATA(val v: String) : Command(), UiDataCommand
    }

    sealed class Result : UiResult {
        object Empty : Result()
    }

    data class ViewState(
            val coverLetter: String = "default",
            val isLengthExceeded: Boolean = false
    ) : UiState
}
