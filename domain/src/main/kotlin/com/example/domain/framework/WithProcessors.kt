package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class WithProcessors(
        vararg val nestedProcessors: Pair<Class<Any>, ObservableTransformer<Any, UiResult>>
) : ObservableTransformer<UiCommand, UiResult> {
    override fun apply(upstream: Observable<UiCommand>): ObservableSource<UiResult> {
        return upstream.publish { shared ->

            val composed =
                    nestedProcessors.map { shared.ofType(it.first).compose<UiResult>(it.second) }.toTypedArray()

            val r = Observable.merge(arrayListOf(*composed))

            r//Observable.empty<UiResult>()
        }
    }

}