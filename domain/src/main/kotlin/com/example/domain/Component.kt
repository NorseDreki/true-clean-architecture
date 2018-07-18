package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

interface Component<C : UiCommand, R : UiResult, S : UiState> {

    val commands: PublishSubject<C>

    val processor: ObservableTransformer<C, R>

    val reducer: ObservableTransformer<R, S>

}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asStandalone(startingCommand: C) =
        StandaloneComponent(this, startingCommand)

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asEmbedded() =
        EmbeddedComponent(this)


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

class EmbeddedComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>
) :
        Component<C, R, S> by component,
        ObservableTransformer<C, R>,
        ViewStateProducer<S> {

    override fun apply(upstream: Observable<C>): ObservableSource<R> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun viewStates(): Observable<S> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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