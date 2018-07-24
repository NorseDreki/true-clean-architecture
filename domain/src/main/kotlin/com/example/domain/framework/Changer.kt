package com.example.domain.framework

import com.example.domain.UiState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class Changer {

    val allViewStates = PublishSubject.create<UiState>()

    fun setTop(viewStates: Observable<UiState>) {
        viewStates.subscribe(allViewStates)
    }

    fun screens(): Observable<UiState> {
        val s = allViewStates
                .materialize()
                .groupBy { it::class.java }

    }
}