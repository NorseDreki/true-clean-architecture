package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.ViewState
import com.example.domain.submitProposal2.common.QuestionViewState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<Result, ViewState> {

    override fun apply(it: Observable<Result>) =

            it.scan(ViewState()) { state, result ->
                when (result) {
                    is Result.QuestionsLoaded ->
                        ViewState(
                                result.questions.map {
                                    QuestionViewState(it.id, it.question, null)
                                }
                        )
                    is Result.ValidAnswer ->
                        ViewState(
                                //state.items.find { it.id == result.id }.answer!! = result.answer
                                state.items.map {
                                    if (it.id == result.id) {
                                        QuestionViewState(it.id, it.question, result.answer)
                                    } else {
                                        it
                                    }
                                }
                        )
                    is Result.EmptyAnswer ->
                        ViewState(
                                //state.items.find { it.id == result.id }.answer!! = result.answer
                                state.items.map {
                                    if (it.id == result.id) {
                                        QuestionViewState(it.id, it.question, null)
                                    } else {
                                        it
                                    }
                                }
                        )
                    else -> state
                }
            }
                    .distinctUntilChanged()!!
}
