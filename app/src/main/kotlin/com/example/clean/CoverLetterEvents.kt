package com.example.clean

import com.example.domain.submitProposal.CoverLetter

class CoverLetterEvents(val coverLetter: CoverLetter) {

    val onTextChanged = ObservableProperty<String>()

    init {
        println("init CLE")
        onTextChanged.observe().subscribe {
            println("00000000 change $it");
            coverLetter.fromEvent(CoverLetter.Command.UpdateCoverLetter(it))
        }
    }
}
