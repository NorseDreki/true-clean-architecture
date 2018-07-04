package com.example.clean

import android.databinding.BaseObservable

import io.reactivex.Observable

class ObservableProperty<T> : BaseObservable {
    private var value: T? = null

    constructor(value: T) {
        this.value = value
    }

    constructor() {}

    fun get(): T? {
        return value
    }

    fun set(value: T?) {
        val isEquals = if (this.value == null) value == null else this.value == value

        if (!isEquals) {
            this.value = value
            notifyChange()
        }
    }

    fun observe(): Observable<T> {
        return RxObservableField.from(this, {get()!!})
    }

    fun observeWithStart(): Observable<T> {
        return observe().startWith(get()!!)
    }
}
