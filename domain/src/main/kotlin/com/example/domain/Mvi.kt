package com.example.domain

import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


interface ViewState

interface Command

interface Result

interface ScreenComponent


interface UiEvent

interface UiCommand
interface UiResult

interface UiState /*{
    class PartialViewState : UiState()
    class ScreenViewState : UiState()
}*/

interface UiActor<C : UiCommand, R : UiResult> {

    fun processCommands(commands: Observable<C>)

    fun produceResults(): Observable<R>
}

interface UiRenderer<S : UiState> {

    fun render(): Observable<S>
}

interface UiComponent<C : UiCommand, R : UiResult, S : UiState> : UiActor<C, R>, UiRenderer<S>

interface UiView {

    fun events(): Observable<UiEvent>

    fun render(state: UiState)
}

interface UiViewModel {

    fun states(): Observable<UiState>

    fun processEvents(events: Observable<UiEvent>)
}


