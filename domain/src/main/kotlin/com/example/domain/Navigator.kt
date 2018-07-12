package com.example.domain

interface Navigator {

    fun <C : UiCommand, R : UiResult, S : UiState> display(component: UiComponent<C, R, S>, command: C)
}