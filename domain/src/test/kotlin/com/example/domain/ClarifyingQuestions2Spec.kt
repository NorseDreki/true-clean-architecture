package com.example.domain

import com.example.domain.models.*
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.ClarifyingQuestions.Command.INIT
import com.example.domain.submitProposal.ClarifyingQuestions.Command.UpdateAnswer
import com.example.domain.submitProposal.ClarifyingQuestions.Result.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class ClarifyingQuestions2Spec : Spek({

    val withNoQuestions = ItemOpportunity(
            ItemDetails("1234", null),
            Proposal(0, null)
    )

    val questions = listOf(
            Question("1", "q1"),
            Question("2", "q1")
    )

    val withQuestions = ItemOpportunity(
            ItemDetails("1234", questions),
            Proposal(0, null)
    )

    val answers = listOf(
            AnsweredQuestion("1", "answer1"),
            AnsweredQuestion("2", "answer2")
    )

    val withAnsweredQuestions = ItemOpportunity(
            ItemDetails("1234", questions),
            Proposal(0, answers)
    )

    component(::ClarifyingQuestions) {

        initialized(INIT(withQuestions)) {

            describe("client initializes with data with non-empty questions") {
                it("should emit those questions") {
                    assertResultAt(0, Questions(questions))
                }
            }

            describe("client updates question with a valid answer") {
                perform {
                    command(UpdateAnswer("1", "answer"))
                }

                it("should emit result with a valid answer") {
                    assertResultAt(2, Valid("1", "answer"))
                }
            }

            describe("client updates a question with answer leading and trailing spaces") {
                perform {
                    command(UpdateAnswer("1", " answer "))
                }

                it("should emit result with a trimmed answer") {
                    assertResultAt(2, Valid("1", "answer"))
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
                    assertResultAt(0, NoQuestions)
                }
            }
        }

        initialized(INIT(withAnsweredQuestions)) {

            describe("client initializes with data with all answered questions") {
                it("should emit status as all questions answered") {
                    assertResultAt(5, AllQuestionsAnswered(true))
                }
            }
        }
    }
})
