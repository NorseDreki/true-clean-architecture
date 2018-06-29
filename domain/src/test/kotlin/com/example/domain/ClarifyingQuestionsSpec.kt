package com.example.domain

import com.example.domain.models.*
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.ClarifyingQuestions.Command.INIT
import com.example.domain.submitProposal.ClarifyingQuestions.Command.UpdateAnswer
import com.example.domain.submitProposal.ClarifyingQuestions.Result.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xit

//@RunWith(JUnitPlatform::class)
class ClarifyingQuestionsSpec : Spek({

    val withNoQuestions = ItemOpportunity(
            ItemDetails("1234", false,null),
            Proposal(0, null, "")
    )

    val questions = listOf(
            Question("1", "q1"),
            Question("2", "q1")
    )

    val withQuestions = ItemOpportunity(
            ItemDetails("1234", false, questions),
            Proposal(0, null, "")
    )

    val answers = listOf(
            AnsweredQuestion("1", "answer1"),
            AnsweredQuestion("2", "answer2")
    )

    val withAnsweredQuestions = ItemOpportunity(
            ItemDetails("1234", false, questions),
            Proposal(0, answers, "")
    )

    component(::ClarifyingQuestions) {

        initialized(INIT(withQuestions)) {

            describe("client initializes with data with non-empty questions") {
                it("should emit those questions") {
                    assertResultAt(0, QuestionsLoaded(questions))
                }

                it("should emit status as 'not answered'") {
                    assertResultAt(1, AllQuestionsAnswered(false))
                }
            }

            describe("client updates question with a valid answer") {
                perform {
                    command(UpdateAnswer("1", "answer"))
                }

                it("should emit result with a valid answer") {
                    assertResultAt(2, ValidAnswer("1", "answer"))
                }
            }

            describe("client updates a question with answer leading and trailing spaces") {
                perform {
                    command(UpdateAnswer("1", " answer "))
                }

                it("should emit result with a trimmed answer") {
                    assertResultAt(2, ValidAnswer("1", "answer"))
                }
            }

            describe("client updates question with answer containing only spaces") {
                perform {
                    command(UpdateAnswer("1", "  "))
                }

                it("should emit result as invalid answer") {
                    assertResultAt(2, EmptyAnswer("1"))
                }
            }

        }

        initialized(INIT(withNoQuestions)) {

            describe("client initialized with data containing no questions") {
                it("should emit result as no questions") {
                    //assertResultAt(0, NoQuestions)
                    assertValuesOnly(NoQuestionsRequired)
                }
            }
        }

        initialized(INIT(withAnsweredQuestions)) {

            describe("client initializes with data with all answered questions") {

                it("should emit status as 'not answered'") {
                    assertResultAt(1, AllQuestionsAnswered(false))
                }

                it("should emit each answer as valid, followed by status update") {
                    assertResultAt(2, ValidAnswer(answers[0].id, answers[0].answer))
                    assertResultAt(3, AllQuestionsAnswered(false))
                    assertResultAt(4, ValidAnswer(answers[1].id, answers[1].answer))
                    assertResultAt(5, AllQuestionsAnswered(true))
                }

                xit("should emit status as all questions answered") {
                    assertResultAt(5, AllQuestionsAnswered(true))
                }
            }

            describe("client deletes changes one answer to invalid") {
                perform {
                    command(UpdateAnswer("1", ""))
                }

                it("should emit answer as invalid") {
                    assertResultAt(6, EmptyAnswer("1"))
                }

                it("should emit status as 'not answered'") {
                    assertResultAt(7, AllQuestionsAnswered(false))
                }
            }
        }
    }
})
