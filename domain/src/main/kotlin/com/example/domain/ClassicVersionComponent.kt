package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

abstract class ClassicVersionComponent<C : UiCommand, R : UiResult, S : UiState> : Actor<C, R>, UiRenderer<S> {

    private val commands = PublishSubject.create<C>()

    protected abstract val processor: ObservableTransformer<C, R>

    protected abstract val reducer: ObservableTransformer<R, S>

    private val stream = commands
            .compose(processor)
            .replay()
            .autoConnect(0)
            //.refCount()

    override fun apply(upstream: Observable<C>): ObservableSource<R> {
        upstream.subscribe(commands)

        return stream
    }

    override fun render(): Observable<S> {
        return stream.compose(reducer)
    }

    fun sendCommand(command: C) {
        commands.onNext(command)
    }
}
