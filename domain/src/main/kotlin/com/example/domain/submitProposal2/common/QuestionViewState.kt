package com.example.domain.submitProposal2.common

import com.example.domain.models.Question

data class QuestionViewState(val id: String, val question: String, val answer: String)


fun List<Question>.toViewState() = this.map {
    QuestionViewState(it.id, it.question, "")
}

fun List<QuestionViewState>.updateAnswer(questionId: String, answer: String) = this.map {
    if (it.id == questionId) {
        QuestionViewState(it.id, it.question, answer)
    } else {
        it
    }
}