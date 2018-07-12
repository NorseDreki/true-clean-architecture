package com.example.clean.screens

import com.example.domain.submitProposal.ClarifyingQuestions

class ClarifyingQuestionsEvents(
        val clarifyingQuestions: ClarifyingQuestions

) : OnItemClickListener {

    override fun onItemClick(item: ClarifyingQuestions.QuestionViewState?, text: CharSequence) {
        clarifyingQuestions.fromEvent(ClarifyingQuestions.Command.UpdateAnswer(item!!.id, text.toString()))
    }
}

interface OnItemClickListener {
    fun onItemClick(item: ClarifyingQuestions.QuestionViewState?, text: CharSequence)
}
