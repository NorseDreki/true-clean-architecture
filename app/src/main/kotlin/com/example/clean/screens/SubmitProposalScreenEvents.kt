package com.example.clean.screens

import com.example.clean.ClarifyingQuestionsEvents

data class SubmitProposalScreenEvents(
        val coverLetter: CoverLetterScreenEvents,
        val clarifyingQuestions: ClarifyingQuestionsEvents
) : ScreenEvents {


}