package com.example.clean

import android.view.View
import com.example.clean.framework.ListBindings
import com.example.clean.framework.ScreenEvents
import com.example.domain.UiState

interface DataBinder {
    fun bind(view: View, state: UiState, events: ScreenEvents, listBindings: ListBindings)
}