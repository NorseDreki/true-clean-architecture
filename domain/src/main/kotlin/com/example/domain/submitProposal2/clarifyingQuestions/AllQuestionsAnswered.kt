package com.example.domain.submitProposal2.clarifyingQuestions

import io.reactivex.Observable
import io.reactivex.ObservableTransformer


val paAnsweredProcessor =
        ObservableTransformer<ClarifyingQuestions.Result, ClarifyingQuestions.Result> { t ->
            t.doOnNext { println("111 $it") }
                    .scan(Processor.AllQuestionsAnswered()) { state, result ->
                        when (result) {
                            is ClarifyingQuestions.Result.QuestionsLoaded -> {
                                state.copy(result.questions.size, mutableSetOf())
                            }
                            is ClarifyingQuestions.Result.NoQuestionsRequired -> {
                                Processor.AllQuestionsAnswered()
                                //state.copy(0, mutableSetOf())
                            }
                            is ClarifyingQuestions.Result.ValidAnswer -> {
                                state.copy(answeredQuestions = state.answeredQuestions.apply { add(result.id) })
                            }
                            is ClarifyingQuestions.Result.EmptyAnswer -> {
                                state.copy(answeredQuestions = state.answeredQuestions.apply { remove(result.id) })
                            }
                        //else -> throw IllegalStateException("sdf")
                            is ClarifyingQuestions.Result.AllQuestionsAnswered -> TODO()
                        }
                    }
                    .skip(1)
                    .flatMap {
                        //implicitly handles "no questions" case
                        if (it.totalQuestions == null) {
                            println("value! $it")
                            Observable.empty<ClarifyingQuestions.Result.AllQuestionsAnswered>()
                        } else {
                            Observable.just(ClarifyingQuestions.Result.AllQuestionsAnswered(it.totalQuestions == it.answeredQuestions.size))
                        }
                    }

        }