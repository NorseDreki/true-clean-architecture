package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.ViewStateProducer
import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicBoolean

class StandaloneComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>,
        private val startingCommand: C
) :
        Component<C, R, S> by component,
        ComponentImplementation,
        ViewStateProducer<S> {

    private val alreadyUsed = AtomicBoolean(false)

    override fun viewStates() =
            if (alreadyUsed.compareAndSet(false, true)) {
                Observable.just(startingCommand)
                        .doOnNext { println("next st") }
                        .doOnDispose { println("dispose st") }
                        .doOnComplete { println("complete st") }
                        .mergeWith(commands.doOnDispose { println("dispose cmd") }
                                .doOnComplete { println("complete cmd") })
                        .compose(processor)
                        .compose(reducer)
                        .doOnDispose { println("dispose") }
                        .doOnComplete { println("complete") }!!
            } else {
                throw IllegalStateException("Must not be used more than once")
            }
}
