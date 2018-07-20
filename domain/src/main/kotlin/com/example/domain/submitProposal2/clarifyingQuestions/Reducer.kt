package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.submitProposal2.common.QuestionViewState
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

class Reducer : ObservableTransformer<ClarifyingQuestions.Result, ClarifyingQuestions.ViewState> {

    override fun apply(it: Observable<ClarifyingQuestions.Result>): ObservableSource<ClarifyingQuestions.ViewState> {
        return it.scan(ClarifyingQuestions.ViewState()) { state, result ->
            when(result) {
                is ClarifyingQuestions.Result.QuestionsLoaded ->
                    ClarifyingQuestions.ViewState(
                            result.questions.map {
                                QuestionViewState(it.id, it.question, null)
                            }
                    )
                is ClarifyingQuestions.Result.ValidAnswer ->
                    ClarifyingQuestions.ViewState(
                            //state.items.find { it.id == result.id }.answer!! = result.answer
                            state.items.map {
                                if (it.id == result.id) {
                                    QuestionViewState(it.id, it.question, result.answer)
                                } else {
                                    it
                                }
                            }
                    )
                is ClarifyingQuestions.Result.EmptyAnswer ->
                    ClarifyingQuestions.ViewState(
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
                .distinctUntilChanged()
    }

}
