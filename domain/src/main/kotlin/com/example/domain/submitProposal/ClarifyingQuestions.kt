package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.submitProposal.ClarifyingQuestions.ViewState
import com.example.domain.submitProposal.ClarifyingQuestions.Result
import com.example.domain.submitProposal.ClarifyingQuestions.Command
import com.example.domain.submitProposal.models.JobDetails
import com.example.domain.submitProposal.models.Question
import io.reactivex.Observable


class ClarifyingQuestions : UiComponent<Command, Result, ViewState> {

    override fun processCommands(commands: Observable<Command>) {
    }

    override fun produceResults(): Observable<Result> {
        return Observable.just(Result.NoQuestions)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class Result : UiResult {
        data class Questions(val questions: List<Question>) : Result()
        object NoQuestions: Result()

        data class Valid(val question: String, val answer: String) : Result()
        data class EmptyAnswer(val question: String) : Result()

        data class AllQuestionsAnswered(val answered: Boolean) : Result()
    }

    sealed class Command : UiCommand {
        data class INIT(val jobDetails: JobDetails/*, val proposal: Proposal*/) : Command()

        //only for internal use? from event
        data class UpdateAnswer(val question: String, val answer: String) : Command()
    }

    data class ViewState(
            val items : List<Question>
    ) : UiState
}
