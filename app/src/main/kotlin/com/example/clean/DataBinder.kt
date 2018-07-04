package com.example.clean

import android.view.View

interface DataBinder {
    fun bind(view: View, state: MainActivity.QuestionsViewStateEvents, events: ClarifyingQuestionsEvents)
}