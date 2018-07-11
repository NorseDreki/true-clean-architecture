package com.example.clean.screens

import com.example.clean.ObservableProperty
import com.example.domain.submitProposal.CoverLetter

class CoverLetterScreenEvents(val coverLetter: CoverLetter) : ScreenEvents {

    val onTextChanged = ObservableProperty<String>()

    init {
        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000 change $it");
            coverLetter.fromEvent(CoverLetter.Command.UpdateCoverLetter(it))
        }
    }
}