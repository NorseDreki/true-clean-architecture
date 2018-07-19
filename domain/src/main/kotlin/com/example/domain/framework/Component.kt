package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

interface Component<C : UiCommand, R : UiResult, S : UiState> {

    val commands: PublishSubject<C>

    val processor: ObservableTransformer<C, R>

    val reducer: ObservableTransformer<R, S>

}

interface ComponentImplementation

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asStandalone(startingCommand: C) =
        StandaloneComponent(this, startingCommand)

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asEmbedded() =
        EmbeddedComponent(this)

fun failOnFinalComponents(component: Component<*, *, *>) {
    check(
            component !is StandaloneComponent
                    && component !is EmbeddedComponent
    ) {
        "Must not be called on final components"
    }
}


fun <T, C, R, S> T.extraCommand(command: C): Boolean
        where T : Component<C, R, S>,
              T : ComponentImplementation,
              C : UiCommand, R : UiResult, S : UiState {


    println("extra command")

    /*kotlin.check(::results.isInitialized) {
        "Can't compose component more than once"
    }*/

    println("has obs ${commands.hasObservers()}")
    println("has complete ${commands.hasComplete()}")


    //better throw exception
    kotlin.io.println("send command")

    if (!commands.hasObservers())
        throw IllegalStateException("Must not issue commands after component is done")

    //if (!commands.hasObservers())
    /*if (commands.hasComplete())
        return false*/

    kotlin.io.println("do send command")

    commands.onNext(command)

    return true
}
