package com.example.clean.screens

import com.example.domain.UiState

interface ScreenEvents

interface Screen {

    val layout: Int

    val state: UiState

    val events: ScreenEvents
}
