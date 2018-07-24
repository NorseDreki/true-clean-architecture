package com.example.domain.framework

import com.example.domain.UiState
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import java.util.*

class Changer {

    val allViewStates =
            PublishSubject.create<Pair<Class<out UiState>, Notification<UiState>>>()

    val pointer = PublishSubject.create<Stack<Class<UiState>>>()

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
                .subscribe(allViewStates)


        //viewStates.subscribe(allViewStates)
    }

    fun screens(): Observable<UiState> {
        val s =
                allViewStates
                        //.groupBy { it.first }
                        .scan(hashMapOf<Class<UiState>, UiState>()) { map, grouped ->
                            if (grouped.second.isOnComplete || grouped.second.isOnError) {
                                map.remove(grouped.first)
                                stack.pop()
                                pointer.onNext(stack)
                            } else {
                                map.put(grouped.first as Class<UiState>, grouped.second.value!!)
                            }

                            map
                        }
                        .withLatestFrom<Stack<Class<UiState>>, UiState>(
                                pointer,
                                BiFunction { t1, t2 ->
                                    t1[t2.peek()]!!
                                }
                        )

        return s
    }
}