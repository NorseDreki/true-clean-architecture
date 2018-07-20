package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.framework.ExtraCommandsComponent
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Question
import com.example.domain.submitProposal2.common.QuestionViewState

class ClarifyingQuestions : ExtraCommandsComponent<ClarifyingQuestions.Command, ClarifyingQuestions.Result, ClarifyingQuestions.ViewState>() {
    override val processor = Processor()
    override val reducer = Reducer()

    sealed class Result : UiResult {
        data class QuestionsLoaded(val questions: List<Question>) : Result()
        object NoQuestionsRequired : Result()

        data class ValidAnswer(val id: String, val answer: String) : Result()
        data class EmptyAnswer(val id: String) : Result()

        data class AllQuestionsAnswered(val answered: Boolean) : Result()
    }

    sealed class Command : UiCommand {
        data class INIT(val itemOpportunity: ItemOpportunity) : Command()

        //only for internal use? from event
        data class UpdateAnswer(val id: String, val answer: String) : Command()
    }


    data class ViewState(
            val items: List<QuestionViewState> = listOf(
                    /*QuestionViewStateEvents(
                            "fd",
                            QuestionViewState("!2","123", null)
                    )*/
            )
    ) : UiState
}
