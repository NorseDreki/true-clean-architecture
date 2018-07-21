package com.example.domain.submitProposal2.clarifyingQuestions

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
        data class INIT(val itemOpportunity: ItemOpportunity) : Command()

        data class UpdateAnswer(val id: String, val answer: String) : Command()
    }

    sealed class Result : UiResult {
        data class QuestionsLoaded(val questions: List<Question>) : Result()
        object NoQuestionsRequired : Result()

        data class ValidAnswer(val id: String, val answer: String) : Result()
        data class EmptyAnswer(val id: String) : Result()

        data class AllQuestionsAnswered(val answered: Boolean) : Result()
    }

    data class ViewState(
            val items: List<QuestionViewState> = listOf()
    ) : UiState

    override val processor = Processor()
    override val reducer = Reducer()
}
