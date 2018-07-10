package com.example.domain.submitProposal;


sealed class DialogState {
    data class Progress(val title: String?, val message: String?) : DialogState()
    data class Alert(val title: String?, val message: String?, val positiveText: String?, val negativeText: String?) : DialogState()
    object Dismissed : DialogState()
}

data class DialogEvents(val onPositive: () -> Unit, val onNegative: () -> Unit)