package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.ReplaySubject

class WithLoopback(
        val inner: ObservableTransformer<UiCommand, UiResult>,
        val resultsToCommands: ObservableTransformer<UiResult, UiCommand>
)
    : ObservableTransformer<UiCommand, UiResult> {

    val loopbackCommands = ReplaySubject.create<UiCommand>()

    override fun apply(upstream: Observable<UiCommand>): ObservableSource<UiResult> {
        val c = upstream
                .mergeWith(loopbackCommands)
                .compose(inner)
                .replay()
                .refCount()

        c.compose(resultsToCommands as ObservableTransformer<in UiResult, out UiCommand>)
                //.takeWhile
                .doOnNext { println("GOT COMMAND: $it") }
                //.delay(10, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("GOT COMMAND SUB $it")
                    loopbackCommands.onNext(it)
                }

        return c
    }
}