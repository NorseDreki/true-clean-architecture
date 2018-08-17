package com.example.clean.framework

import com.example.domain.UiState

interface ScreenEvents

interface ListBindings

interface Screen {

    val layout: Int

    val state: UiState

    val events: ScreenEvents

    val listBindings: ListBindings
}
