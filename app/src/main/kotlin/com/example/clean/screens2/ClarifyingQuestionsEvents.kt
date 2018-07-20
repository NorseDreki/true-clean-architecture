package com.example.clean.screens2

import com.example.domain.framework.extraCommand
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.common.QuestionViewState

class ClarifyingQuestionsEvents(
        val clarifyingQuestions: ClarifyingQuestions

) : OnItemClickListener {

    override fun onItemClick(item: QuestionViewState?, text: CharSequence) {
        clarifyingQuestions.extraCommand(ClarifyingQuestions.Command.UpdateAnswer(item!!.id, text.toString()))
    }
}

interface OnItemClickListener {
    fun onItemClick(item: QuestionViewState?, text: CharSequence)
}
