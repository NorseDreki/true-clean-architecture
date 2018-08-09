package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.StartCommand
import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Question
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.*
import com.example.domain.submitProposal2.common.QuestionViewState

class ClarifyingQuestions : ExtraCommandsComponent<Command, Result, ViewState>() {

    sealed class Command : UiCommand {
        data class START(val itemOpportunity: ItemOpportunity) : Command(), StartCommand

        data class UpdateAnswer(val questionId: String, val answer: String) : Command()
    }

    sealed class Result : UiResult {
        object NotRequired : Result()

        data class QuestionsLoaded(val questions: List<Question>) : Result()

        data class AnswerValid(val questionId: String, val answer: String) : Result()
        data class AnswerEmpty(val questionId: String) : Result()

        data class AnsweredQuestionsCount(val answeredCount: Int, val totalCount: Int) : Result()
    }

    data class ViewState(
            val isVisible: Boolean = true,
            val questions: List<QuestionViewState> = listOf()
    ) : UiState


    override val processor = ClarifyingQuestionsProcessor()
    override val reducer = ClarifyingQuestionsReducer()
}
