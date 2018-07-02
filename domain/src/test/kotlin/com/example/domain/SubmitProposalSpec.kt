package com.example.domain

import com.example.domain.models.*
import com.example.domain.submitProposal.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xit

class SubmitProposalSpec : Spek({

    val proposeTermsOnly = ItemDetails("1234", false, null)

    val sp = {
        val cl = CoverLetter()
        val cq = ClarifyingQuestions()

        SubmitProposal(cl, cq)
    }

    val questions = listOf(
            Question("1", "q1"),
            Question("2", "q1")
    )

    val answers = listOf(
            AnsweredQuestion("1", "answer1"),
            AnsweredQuestion("2", "answer2")
    )


    val everythingFilled = ItemDetails("1234", true, questions)

    val proposal = Proposal(0, null, "")


    component(sp) {


        initialized(SubmitProposal.Command.DATA(proposeTermsOnly)) {

            describe("client gives control to submit proposal by giving it data") {

                it("should initialize components with data?") {
                    assertResultAt(0, SubmitAllowedResult.Disabled)
                    assertResultAt(1, SubmitProposal.Result.ProposalLoaded(
                            ItemOpportunity(proposeTermsOnly, proposal))
                    )
                    assertResultAt(2, CoverLetter.Result.NoCoverLetterRequired)
                    assertResultAt(3, ClarifyingQuestions.Result.NoQuestionsRequired)
                    assertResultAt(4, SubmitAllowedResult.Enabled)

                }

                xit("should open panel in an anchored state") {

                }
            }

            /*describe("data is updated and new data is propagated") {

                perform {
                    command(SubmitProposal.Command.DATA(proposeTermsOnly.copy(isCoverLetterRequired = true)))
                }

                it("should disallow submit") {
                    assertResultAt(4, CoverLetter.Result.Empty)
                    assertResultAt(5, ClarifyingQuestions.Result.NoQuestionsRequired)
                    assertResultAt(6, SubmitAllowedResult.Disabled)
                }
            }*/

        }

        initialized(SubmitProposal.Command.DATA(everythingFilled)) {

            describe("client gives control to submit proposal with full data") {

                it("should initialize components with data?") {
                    assertResultAt(0, SubmitAllowedResult.Disabled)
                    assertResultAt(1, SubmitProposal.Result.ProposalLoaded(
                            ItemOpportunity(everythingFilled, proposal))
                    )
                    assertResultAt(2, CoverLetter.Result.Empty)
                    assertResultAt(3, SubmitProposal.Result.ProposalUpdated)
                    assertResultAt(4, ClarifyingQuestions.Result.QuestionsLoaded(questions))
                    assertResultAt(5, ClarifyingQuestions.Result.AllQuestionsAnswered(false))

                }

            }

            describe("client fills all steps of submit proposal") {
                perform {
                    (deps.component as SubmitProposal).coverLetter
                            .fromEvent(CoverLetter.Command.UpdateCoverLetter("coverLetter"))

                    (deps.component as SubmitProposal).clarifyingQuestions
                            .fromEvent(ClarifyingQuestions.Command.UpdateAnswer("1", "answer1"))

                    (deps.component as SubmitProposal).clarifyingQuestions
                            .fromEvent(ClarifyingQuestions.Command.UpdateAnswer("2", "answer2"))

                    (deps.component as SubmitProposal)
                            .fromEvent(DoSubmitProposalCommand.DoSubmit(proposal))
                }

                it("should have all steps filled") {
                    assertResultAt(6, CoverLetter.Result.Valid("coverLetter"))
                    assertResultAt(7, SubmitProposal.Result.ProposalUpdated)
                    assertResultAt(8, ClarifyingQuestions.Result.ValidAnswer("1", "answer1"))
                    assertResultAt(9, SubmitProposal.Result.ProposalUpdated)
                    assertResultAt(10, ClarifyingQuestions.Result.AllQuestionsAnswered(false))
                    assertResultAt(11, ClarifyingQuestions.Result.ValidAnswer("2", "answer2"))
                    assertResultAt(12, SubmitProposal.Result.ProposalUpdated)
                    assertResultAt(13, ClarifyingQuestions.Result.AllQuestionsAnswered(true))
                    assertResultAt(14, SubmitAllowedResult.Enabled)
                }

                //do not allow processing "DoSubmitProposal" when submit not allowed
                //make an explicit state machine

                it("should send submit proposal") {
                    assertResultAt(15, DoSubmitProposalResult.InProgress)
                    assertResultAt(16, DoSubmitProposalResult.Success("response"))
                    assertResultAt(17, SubmitProposal.Result.ProposalSent)
                    assertResultAt(18, SubmitProposal.Result.ProposalRemoved)
                }

            }
        }
    }
})
