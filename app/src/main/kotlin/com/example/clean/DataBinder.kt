package com.example.clean

import android.view.View
import com.example.clean.screens.ScreenEvents
import com.example.domain.UiState

interface DataBinder {
    fun bind(view: View, state: UiState, events: ScreenEvents)
}