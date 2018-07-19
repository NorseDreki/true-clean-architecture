package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

class EmbeddedComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>
) :
        Component<C, R, S> by component,
        ComponentImplementation {

}