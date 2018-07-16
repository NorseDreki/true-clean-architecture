package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

abstract class ClassicVersionComponent<C : UiCommand, R : UiResult, S : UiState> : Actor<C, R>, UiRenderer<S> {

    private val commands = PublishSubject.create<C>()

    protected abstract val processor: ObservableTransformer<C, R>

    protected abstract val reducer: ObservableTransformer<R, S>

    private lateinit var results: Observable<R>

    override fun apply(upstream: Observable<C>): ObservableSource<R> {
        check(::results.isInitialized) {
            "Can't compose component more than once"
        }

        val transformed = upstream
                .mergeWith(commands)
                .compose(processor)
                .replay()
                .refCount()
                //maybe two lines up
                .takeUntil(upstream.materialize().filter { it.isOnComplete })

        results = transformed

        return transformed
    }

    override fun render(): Observable<S> {
        checkNotNull(results) {
            "Render() must be called only after composing component"
        }

        return results.compose(reducer)
    }

    fun sendCommand(command: C) {
        commands.onNext(command)
    }
}
