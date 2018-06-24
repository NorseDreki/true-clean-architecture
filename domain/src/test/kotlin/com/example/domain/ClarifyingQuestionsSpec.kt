package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.models.Question
import com.example.domain.submitProposal.ClarifyingQuestions
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

//@RunWith(JUnitPlatform::class)
class ClarifyingQuestionsSpec : Spek({


    given("a ClarifyingQuestions component") {
        //val cq by memoized(CachingMode.SCOPE) { ClarifyingQuestions() }
        /*val cmd
                by memoized(CachingMode.GROUP) { println("subj");PublishSubject.create<ClarifyingQuestions.Command>() }

        val sub by memoized(CachingMode.GROUP) {
            val cq = ClarifyingQuestions()
            println("testing")
            cq.process(cmd).test()
        }*/

        /*val cq = ClarifyingQuestions()
        val sub = cq.process(cmd).test()*/

        context("item details has no questions") {
            val itemOpportunity = ItemOpportunity(
                    ItemDetails("1234", null),
                    Proposal(0, null)
            )
            val cmd = PublishSubject.create<ClarifyingQuestions.Command>()
            val cq = ClarifyingQuestions()
            val sub = cq.process(cmd).test()

            on("cq is initialized with no questions") {
                cmd.onNext(ClarifyingQuestions.Command.INIT(itemOpportunity))
                println("onnext")

                it("sends result as no questions") {
                    /*cq
                            .process(cmd)
                            .test()*/
                      sub      .assertValue(ClarifyingQuestions.Result.NoQuestions)
                }
            }

            context("questions are updated") {
                val questions = listOf(Question("1", "q1"), Question("2", "q1"))
                val itemOpportunity = ItemOpportunity(
                        ItemDetails("1234", questions),
                        Proposal(0, null)
                )

                on("but then updates with some") {
                    cmd.onNext(ClarifyingQuestions.Command.INIT(itemOpportunity))
                    println("some")

                    it("") {
                        sub.assertValueAt(1, ClarifyingQuestions.Result.Questions(questions))
                    }

                }
            }

        }

        context("some questions") {
            val cmd = PublishSubject.create<ClarifyingQuestions.Command>()
            val cq = ClarifyingQuestions()
            val sub = cq.process(cmd).test()

            val questions = listOf(Question("1", "q1"), Question("2", "q1"))
            val itemOpportunity = ItemOpportunity(
                    ItemDetails("1234", questions),
                    Proposal(0, null)
            )
            on("component is told to initialize") {
                cmd.onNext(ClarifyingQuestions.Command.INIT(itemOpportunity))

                it("should have some questions") {


                    sub.assertValueAt(0, ClarifyingQuestions.Result.Questions(questions))
                }
            }

            context("user answers questions") {
                on("client puts a valid answer") {
                    cmd.onNext(ClarifyingQuestions.Command.UpdateAnswer("1", "answer"))

                    it("should result in valid answer") {
                        sub.assertValueAt(1, ClarifyingQuestions.Result.Valid("1", "answer"))
                        println("1")
                    }

                    /*it("should still have questions as not valid") {

                    }*/

                    cmd.onNext(ClarifyingQuestions.Command.UpdateAnswer("2", "answer2"))

                    it("should result in valid answer 2") {
                        sub.assertValueAt(2, ClarifyingQuestions.Result.Valid("2", "answer2"))

                        println("2")
                    }
                }
                /*on("client answers all questions correctly") {
                    cmd.onNext(ClarifyingQuestions.Command.UpdateAnswer("2", "answer2"))

                    it("should result in valid answer") {
                        sub.assertValueAt(2, ClarifyingQuestions.Result.Valid("2", "answer2"))

                        println("2")
                    }
                }*/
            }
         }
    }})

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

    /*given("a ClarifyingQuestions component") {
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
*/
