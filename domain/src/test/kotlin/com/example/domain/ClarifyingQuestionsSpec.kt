package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions
import io.reactivex.Observable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ClarifyingQuestionsSpec : Spek({

    given("a ClarifyingQuestions component") {
        context("item details has no questions") {
            val cq = ClarifyingQuestions()

            on("component is told to initialize") {
                val itemOpportunity = ItemOpportunity(
                        ItemDetails("1234", null),
                        Proposal(0, null)
                )
                val commands =
                        Observable.just<ClarifyingQuestions.Command>(ClarifyingQuestions.Command.INIT(itemOpportunity))

                cq.acceptCommands(commands)

                it("should have no questions") {


                    cq.publishResults().test().assertValue(ClarifyingQuestions.Result.NoQuestions)
                }
            }
        }

        context("item details has some questions") {
            val cq = ClarifyingQuestions()

            on("component is told to initialize") {

                val questions = listOf(Question("1", "q1"), Question("2", "q1"))
                val itemOpportunity = ItemOpportunity(
                        ItemDetails("1234", questions),
                        Proposal(0, null)
                )
                val commands =
                        Observable.just<ClarifyingQuestions.Command>(
                                ClarifyingQuestions.Command.INIT(itemOpportunity),
                                ClarifyingQuestions.Command.UpdateAnswer("1", "answer"),
                                ClarifyingQuestions.Command.UpdateAnswer("2", "answer")
                        )

                cq.acceptCommands(commands)

                it("should have some questions") {


                    cq.publishResults().test().assertValueAt(0, ClarifyingQuestions.Result.Questions(questions))
                }



                it("should have answer updated") {
                    cq.publishResults().test().assertValueAt(1, ClarifyingQuestions.Result.Valid("1", "answer"))
                }

                it("should mark all questions as valid") {
                    cq.publishResults().test().assertValueAt(2, ClarifyingQuestions.Result.AllQuestionsAnswered(true))
                }

                on("user enters empty answer for a question") {
                    it("should mark that question as invalid") {

                    }

                    it("should mark validation as failed") {

                    }
                }

            }


        }
    }

})
