package com.example.clean.screens

data class SubmitProposalScreenEvents(
        val coverLetter: CoverLetterScreenEvents,
        val clarifyingQuestions: ClarifyingQuestionsEvents
) : ScreenEvents {


}