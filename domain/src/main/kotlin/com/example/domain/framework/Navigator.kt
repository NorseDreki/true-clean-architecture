package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState

interface Navigator {

    fun <C : UiCommand, R : UiResult, S : UiState> display(component: Component<C, R, S>, command: C)
}