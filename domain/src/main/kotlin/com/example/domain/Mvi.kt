package com.example.domain

import io.reactivex.Observable


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

    fun acceptCommands(commands: Observable<C>)

    fun publishResults(): Observable<R>

    fun process(commands: Observable<C>): Observable<R>
}

interface UiRenderer<S : UiState> {

    fun render(): Observable<S>
}

interface UiComponent<C : UiCommand, R : UiResult, S : UiState> : UiActor<C, R>, UiRenderer<S>




interface UiThunk<R1 : UiResult, R2 : UiResult>





interface UiView {

    fun events(): Observable<UiEvent>

    fun render(state: UiState)
}

interface UiViewModel {

    fun states(): Observable<UiState>

    fun processEvents(events: Observable<UiEvent>)
}


