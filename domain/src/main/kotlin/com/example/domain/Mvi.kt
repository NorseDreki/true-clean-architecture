package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer


/*
interface ViewState

interface Command

interface Result
*/

interface ScreenComponent

interface DataCommand : UiCommand

interface UiEvent

interface UiCommand
interface UiDataCommand
interface StartCommand
interface UiResult

interface UiState /*{
    class PartialViewState : UiState()
    class ScreenViewState : UiState()
}*/

interface Actor<C : UiCommand, R : UiResult> : ObservableTransformer<C, R>


interface Processor<C : UiCommand, R : UiResult> : ObservableTransformer<C, R>
interface Reducer<R : UiResult, S : UiState> : ObservableTransformer<R, S>
interface Thunk<R1 : UiResult, R2 : UiResult> : ObservableTransformer<R1, R2>
interface Loopback<R : UiResult, C : UiCommand> : ObservableTransformer<R, C>


interface UiActor<C : UiCommand, R : UiResult> {

/*
    fun acceptCommands(commands: Observable<C>)

    fun publishResults(): Observable<R>
*/

    fun process(commands: Observable<C>): Observable<R>
}

interface UiRenderer<S : UiState> {

    fun render(): Observable<S>
}

interface UiComponent<C : UiCommand, R : UiResult, S : UiState> : UiActor<C, R>, UiRenderer<S>




interface UiThunk<R1 : UiResult, R2 : UiResult>



//ViewStateSeeder
interface ViewStateProducer<VS : UiState> {

    fun viewStates(): Observable<VS>
}




interface UiView {

    fun events(): Observable<UiEvent>

    fun render(state: UiState)
}

interface UiViewModel {

    fun states(): Observable<UiState>

    fun processEvents(events: Observable<UiEvent>)
}


