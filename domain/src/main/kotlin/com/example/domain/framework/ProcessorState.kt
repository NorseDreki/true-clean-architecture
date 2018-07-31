package com.example.domain.framework

import com.example.domain.DataCommand
import com.example.domain.UiCommand
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