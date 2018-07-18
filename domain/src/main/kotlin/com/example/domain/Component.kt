package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

abstract class Component<C : UiCommand, R : UiResult, S : UiState> {


    private val commands = PublishSubject.create<C>()

    protected abstract val processor: ObservableTransformer<C, R>

    protected abstract val reducer: ObservableTransformer<R, S>

    private lateinit var results: Observable<R>

    fun sendCommand(command: C): Boolean {
        check(::results.isInitialized) {
            "Can't compose component more than once"
        }


        //better throw exception
        println("send command")

        //if (!commands.hasObservers())
        /*if (commands.hasComplete())
            return false*/

        println("do send command")

        commands.onNext(command)

        return true
    }
}
