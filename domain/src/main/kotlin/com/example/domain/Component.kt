package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

interface Component<C : UiCommand, R : UiResult, S : UiState> {

    val commands: PublishSubject<C>

    val processor: ObservableTransformer<C, R>

    val reducer: ObservableTransformer<R, S>

}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asStandalone(startingCommand: C) =
        StandaloneComponent(this, startingCommand)


class StandaloneComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>,
        private val startingCommand: C
) :
        Component<C, R, S> by component,
        ViewStateProducer<S> {

    override fun viewStates(): Observable<S> {
        return Observable.just(startingCommand)
                .mergeWith(commands)
                .compose(processor)
                .compose(reducer)
        //or subject will be auto-disposed?
        //.doOnDispose { commands.onComplete() }
    }
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