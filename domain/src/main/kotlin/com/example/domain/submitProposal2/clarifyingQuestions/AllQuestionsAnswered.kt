package com.example.domain.submitProposal2.clarifyingQuestions

import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


val paAnsweredProcessor =
        ObservableTransformer<Result, Result> { t ->
            t.doOnNext { println("111 $it") }
                    .scan(Processor.AllQuestionsAnswered()) { state, result ->
                        when (result) {
                            is Result.QuestionsLoaded -> {
                                state.copy(result.questions.size, mutableSetOf())
                            }
                            is Result.NoQuestionsRequired -> {
                                Processor.AllQuestionsAnswered()
                                //state.copy(0, mutableSetOf())
                            }
                            is Result.ValidAnswer -> {
                                state.copy(answeredQuestions = state.answeredQuestions.apply { add(result.id) })
                            }
                            is Result.EmptyAnswer -> {
                                state.copy(answeredQuestions = state.answeredQuestions.apply { remove(result.id) })
                            }
                        //else -> throw IllegalStateException("sdf")
                            is Result.AllQuestionsAnswered -> TODO()
                        }
                    }
                    .skip(1)
                    .flatMap {
                        //implicitly handles "no questions" case
                        if (it.totalQuestions == null) {
                            println("value! $it")
                            Observable.empty<Result.AllQuestionsAnswered>()
                        } else {
                            Observable.just(Result.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size))
                        }
                    }

        }