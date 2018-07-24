package com.example.domain.framework

import com.example.domain.UiState
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.ReplaySubject
import java.util.*

class Changer {

    val allViewStates =
            ReplaySubject.create<Pair<Class<out UiState>, Notification<UiState>>>()

    val pointer = ReplaySubject.create<Stack<Class<UiState>>>()

    val stack = Stack<Class<UiState>>()

    fun setTop(viewStates: Observable<UiState>) {
        viewStates.materialize()
                .withLatestFrom<UiState, Pair<Class<out UiState>, Notification<UiState>>>(
                        viewStates
                                .firstOrError()
                                .toObservable()
                                .doOnNext {
                                    stack.push(it::class.java as Class<UiState>)
                                    pointer.onNext(stack)
                                },
                        BiFunction { notification, uiState ->
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

    fun screens(): Observable<UiState> {
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

        return s
    }
}