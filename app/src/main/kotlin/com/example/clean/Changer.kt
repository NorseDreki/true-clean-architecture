package com.example.clean

import com.example.clean.screens.Screen
import com.example.domain.UiState
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.ReplaySubject
import java.util.*

class Changer {

    val allViewStates =
            ReplaySubject.create<Pair<Class<out UiState>, Notification<UiState>>>()

    val pointer = ReplaySubject.create<Stack<Class<UiState>>>()

    val stack = Stack<Class<UiState>>()

    val toScreens =
            hashMapOf<Class<UiState>, ObservableTransformer<UiState, Screen>>()

    fun setTop(viewStates: Observable<UiState>, toS: Pair<Class<UiState>, ObservableTransformer<UiState, Screen>>) {
        toScreens.put(toS.first, toS.second)
        println("TOSCREENS $toScreens")

        viewStates.materialize()
                .withLatestFrom<UiState, Pair<Class<out UiState>, Notification<UiState>>>(
                        viewStates
                                .firstOrError()
                                .toObservable()
                                .doOnNext {
                                    println("NEW VIEWSTATES FIRST OR ERR $it")
                                    stack.push(it::class.java as Class<UiState>)
                                    pointer.onNext(stack)
                                },
                        BiFunction { notification, uiState ->
                            println("COMBINING")
                            uiState::class.java to notification
                        }
                )
                .doOnNext {
                    println("SETTOP next: $it")
                    println("$stack")
                }
                .subscribe(allViewStates)


        //viewStates.subscribe(allViewStates)
    }

    fun screens(): Observable<Screen> {
        val s =
                allViewStates
                        //.groupBy { it.first }
                        .scan(hashMapOf<Class<UiState>, UiState>()) { map, grouped ->
                            println("SCAN11")
                            if (grouped.second.isOnComplete || grouped.second.isOnError) {
                                map.remove(grouped.first)
                                stack.pop()
                                pointer.onNext(stack)
                            } else {
                                map.put(grouped.first as Class<UiState>, grouped.second.value!!)
                            }

                            map
                        }.skip(1)
                        .withLatestFrom<Stack<Class<UiState>>, UiState>(
                                pointer,
                                BiFunction { t1, t2 ->
                                    t1[t2.peek()]!!
                                }
                        )
                        .doOnNext {
                            println("ALL VIEW STATES: $it")
                        }

        return s.compose {
            it.map {
                Observable.just(it).compose(toScreens.get(it::class.java))
            }
        }.flatMap { it }
    }
}
