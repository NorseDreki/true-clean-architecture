package com.example.domain

import io.reactivex.ObservableTransformer

class DummyComponent : ClassicVersion2Component<DummyComponent.Command, DummyComponent.Result, DummyComponent.ViewState>() {

    override val processor = ObservableTransformer<Command, Result> {
        it.map { Result.Empty }
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
