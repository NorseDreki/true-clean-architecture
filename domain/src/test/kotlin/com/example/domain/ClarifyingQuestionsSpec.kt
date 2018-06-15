package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.models.JobDetails
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
                                ClarifyingQuestions.Command.UpdateAnswer("1", "answer")
                        )

                cq.acceptCommands(commands)

                it("should have some questions") {


                    cq.publishResults().test().assertValueAt(0, ClarifyingQuestions.Result.Questions(questions))
                }

                it("should have answer updated") {
                    cq.publishResults().test().assertValueAt(1, ClarifyingQuestions.Result.Valid("1", "answer"))
                }

                on("user entered answer for a question") {


                }
            }


        }
    }

})
