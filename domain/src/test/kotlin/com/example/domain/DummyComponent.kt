package com.example.domain

import io.reactivex.ObservableTransformer

class DummyComponent : ClassicVersionComponent<DummyComponent.Command, DummyComponent.Result, DummyComponent.ViewState>() {

    override val processor = ObservableTransformer<Command, Result> {
        it.map { Result.Empty }
    }
    override val reducer = ObservableTransformer<Result, ViewState> {
        it.map { ViewState() }
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
