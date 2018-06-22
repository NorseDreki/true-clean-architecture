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


    val cq by memoized {  }

    /*

    feature {

         scenario cq with no questions
             given cq created
             when cq is initialized with no questions
             then it sends result as no questions
             and generates empty view state

                scenario cq with no questions but then updates with some
                    given cq created
                    and cq is initialized with no questions
                    when questions show up
                    then it sends result as questions
                    and generates questions view state

         scenario cq with questions
             given cq created
             and there are no answers
             when cq is initialized with questions
             then it sends result as questions
             and sends step validity as false
             and generates questions view state

                scenario update answer when entered
                    given cq is initialized with questions
                    when user enters valid answer
                    then it sends result as questions AND answers
                    and sends step validity as true
                    and generates questions view state WITH answers

                scenario load stored answers
                    given there are previously saved answers to ALL questions
                    when cq is initialized with questions
                    then it sends result as questions AND answers
                    and sends step validity as true
                    and generates questions view state WITH answers

                    scenario update answer when cleared
                        given cq is initialized with questions
                        and all answers
                        when user clears an answer
                        then is sends empty answer
                        and sends step validity as false





    }

     */

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
