package com.example.clean.screens2

import com.example.clean.ObservableProperty
import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.coverLetter.CoverLetter

class CoverLetterScreenEvents(val coverLetter: CoverLetter) : ScreenEvents {

    val onTextChanged = ObservableProperty<String>()

    init {
        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000 change $it");
            coverLetter.extraCommand(CoverLetter.Command.Update(it))
        }
    }
}