package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiComponent
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.models.ItemOpportunity
import com.example.domain.submitProposal.ClarifyingQuestions.ViewState
import com.example.domain.submitProposal.ClarifyingQuestions.Result
import com.example.domain.submitProposal.ClarifyingQuestions.Command
import com.example.domain.models.Question
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject


class ClarifyingQuestions : UiComponent<Command, Result, ViewState> {

    val results = ReplaySubject.create<UiResult>()

    override fun acceptCommands(commands: Observable<Command>) {
        commands
                .doOnNext { println(it) }
                .compose(paProcessor)
                .doOnNext { println(it) }
                .subscribe(results)
    }

    override fun publishResults(): Observable<Result> {
        return results.cast(Result::class.java)
    }

    override fun render(): Observable<ViewState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val paProcessor =
            ObservableTransformer<UiCommand, UiResult> { t ->
                t.map {
                    when (it) {
                        is Command.INIT -> {
                            if (it.itemOpportunity.itemDetails.questions != null) {
                                Result.Questions(it.itemOpportunity.itemDetails.questions)
                            } else {
                                Result.NoQuestions
                            }
                        }
                        is Command.UpdateAnswer -> {
                            val validated = it.answer.trim()


                            if (validated.isNotEmpty()) {
                                Result.Valid(it.id, it.answer)
                            } else {
                                Result.EmptyAnswer(it.id)
                            }
                        }
                        else -> throw IllegalStateException("sdf")
                    }
                }
            }


    sealed class Result : UiResult {
        data class Questions(val questions: List<Question>) : Result()
        object NoQuestions: Result()

        data class Valid(val id: String, val answer: String) : Result()
        data class EmptyAnswer(val question: String) : Result()

        data class AllQuestionsAnswered(val answered: Boolean) : Result()
    }

    sealed class Command : UiCommand {
        data class INIT(val itemOpportunity: ItemOpportunity) : Command()

        //only for internal use? from event
        data class UpdateAnswer(val id: String, val answer: String) : Command()
    }

    data class ViewState(
            val items : List<Question>
    ) : UiState
}
