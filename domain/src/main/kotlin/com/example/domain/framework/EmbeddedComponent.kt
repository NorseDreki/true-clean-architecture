package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import io.reactivex.ObservableTransformer

class EmbeddedComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>
) :
        Component<C, R, S> by component,
        ComponentImplementation {

    val asActor =
            ObservableTransformer<C, R> {
                it
                        .mergeWith(commands)
                        .compose(processor)
            }

    val asReducer =
            ObservableTransformer<R, S> {
                it
                        .compose(reducer)
            }
}
