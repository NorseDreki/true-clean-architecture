package com.example.domain

import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

interface UiEvent

interface UiCommand
interface UiResult


sealed class UiState {
    class PartialViewState : UiState()
    class ScreenViewState : UiState()
}

interface UiActor {

    fun processCommands(commands: Observable<UiCommand>)

    fun produceResults(): Observable<UiResult>
}

interface UiRenderer {

    fun render(): Observable<UiState>

    fun asTransformer(): ObservableTransformer<UiResult, UiState>
}

interface UiComponent : UiActor, UiRenderer

interface UiView {

    fun events(): Observable<UiEvent>

    fun render(state: UiState)
}

interface UiViewModel {

    fun states(): Observable<UiState>

    fun processEvents(events: Observable<UiEvent>)
}

