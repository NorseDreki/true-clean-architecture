package com.example.domain.framework

import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class WithResults<R : UiResult>(
        private vararg val processors: ObservableTransformer<R, R>
) : ObservableTransformer<R, R> {

    override fun apply(upstream: Observable<R>) =
        upstream.publish { shared ->

            val composed =
                    processors.map { shared.compose(it) }.toTypedArray()

            Observable.merge(arrayListOf(shared, *composed))
        }!!
}
