package com.example.clean

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

import io.reactivex.Observable


object RxObservableField {

    fun <T> from(field: ObservableField<T>): Observable<T> {
        return from(field, field::get)
    }

    fun from(field: ObservableBoolean): Observable<Boolean> {
        return from(field, field::get)
    }

    fun <T> from(field: android.databinding.Observable, getter: () -> T): Observable<T> {
        return Observable.create { subscriber ->
            field.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: android.databinding.Observable, propertyId: Int) {
                    //TODO fix unsubscription: remove OnPropertyChangedCallback for each
                    //TODO unsubscribed subscriber
                    //if (!subscriber.isUnsubscribed()) {
                    if (!subscriber.isDisposed) {
                        subscriber.onNext(getter())
                    }
                }
            })
        }
    }
}
