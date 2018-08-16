package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

interface DataCommand : UiCommand
interface UiDataCommand
interface StartCommand

interface UiCommand
interface UiResult
interface UiState

interface Processor<C : UiCommand, R : UiResult> : ObservableTransformer<C, R>
interface Reducer<R : UiResult, S : UiState> : ObservableTransformer<R, S>
interface Thunk<R1 : UiResult, R2 : UiResult> : ObservableTransformer<R1, R2>
interface Loopback<R : UiResult, C : UiCommand> : ObservableTransformer<R, C>

//ViewStateSeeder
interface ViewStateProducer<VS : UiState> {

    fun viewStates(): Observable<VS>
}


/////////

interface UiActor<C : UiCommand, R : UiResult> {

/*
    fun acceptCommands(commands: Observable<C>)

    fun publishResults(): Observable<R>
*/

    fun process(commands: Observable<C>): Observable<R>
}

interface Actor<C : UiCommand, R : UiResult> : ObservableTransformer<C, R>

interface UiRenderer<S : UiState> {

    fun render(): Observable<S>
}

interface UiComponent<C : UiCommand, R : UiResult, S : UiState> : UiActor<C, R>, UiRenderer<S>
