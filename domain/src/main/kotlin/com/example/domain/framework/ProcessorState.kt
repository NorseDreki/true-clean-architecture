package com.example.domain.framework

import com.example.domain.DataCommand
import com.example.domain.UiCommand
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

data class ProcessorState(
        val dataCommand: DataCommand,
        val currentCommand: UiCommand
)

val withData =
        ObservableTransformer<UiCommand, ProcessorState> {
            it.publish { shared ->
                shared.withLatestFrom(
                        shared
                                .skipWhile{ it !is DataCommand }
                                .firstOrError()
                                .cast(DataCommand::class.java)
                                .toObservable(),

                        BiFunction<UiCommand, DataCommand, ProcessorState> { t1, t2 ->
                            ProcessorState(t2, t1)
                        }
                )
            }
        }

data class Memoized<T>(val memo: Memoizable, val current: T) {

}

class WithMemoized<T> : ObservableTransformer<T, Memoized<T>> {

    override fun apply(upstream: Observable<T>): ObservableSource<Memoized<T>> {
        return upstream.publish { shared ->
            shared.withLatestFrom(
                    shared
                            .filter { it is Memoizable }
                            //.firstOrError()
                            //.cast(DataCommand::class.java)
                            //.toObservable(),
                            ,

                    BiFunction<T, T, Memoized<T>> { t1, t2 ->
                        println("MEMOIZED $t1, $t2")
                        Memoized(t2 as Memoizable, t1)
                    }
            )
        }
    }
}
