package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

abstract class Component<C : UiCommand, R : UiResult, S : UiState> {


    internal val commands = PublishSubject.create<C>()

    internal abstract val processor: ObservableTransformer<C, R>

    protected abstract val reducer: ObservableTransformer<R, S>

    internal lateinit var results: Observable<R>


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

interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() { print(x) }
}

class Derived(b: Base) : Base by b

fun main(args: Array<String>) {
    val b = BaseImpl(10)
    Derived(b).print()
}