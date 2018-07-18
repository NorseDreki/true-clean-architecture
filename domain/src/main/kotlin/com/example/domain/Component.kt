package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

interface Component<C : UiCommand, R : UiResult, S : UiState> {


    val commands : PublishSubject<C>

    val processor: ObservableTransformer<C, R>

    val reducer: ObservableTransformer<R, S>

    val results: Observable<R>


}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asStandalone() =
        StandaloneComponent(this)


class StandaloneComponent<C : UiCommand, R : UiResult, S : UiState>(val component: Component<C, R, S>) :
        Component<C, R, S> by component {


}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.extraCommand(command: C): Boolean {
    /*kotlin.check(::results.isInitialized) {
        "Can't compose component more than once"
    }*/


    //better throw exception
    kotlin.io.println("send command")

    //if (!commands.hasObservers())
    /*if (commands.hasComplete())
        return false*/

    kotlin.io.println("do send command")

    commands.onNext(command)

    return true
}

fun test() {
    //val c =
}