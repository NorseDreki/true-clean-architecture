package com.example.domain

import io.reactivex.Observable

class BaseComponent<C : UiCommand, R : UiResult, S : UiState> : UiComponent<C, R, S> {





    override fun process(commands: Observable<C>): Observable<R> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(): Observable<S> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}