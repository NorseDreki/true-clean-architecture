package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.submitProposal.ClarifyingQuestions
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.SubmitProposal
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xit

class SubmitProposalSpec : Spek({

    val itemDetails = ItemDetails("1234", false, null)

    val sp = {
        val cl = CoverLetter()
        val cq = ClarifyingQuestions()

        SubmitProposal(cl, cq)
    }

    component(sp) {

        initialized(SubmitProposal.Command.DATA(itemDetails)) {

            describe("client gives control to submit proposal by giving it data") {

                it("should initialize components with data?") {
                    assertResultAt(0, CoverLetter.Result.NoCoverLetterRequired)
                    assertResultAt(1, ClarifyingQuestions.Result.NoQuestions)

                }

                xit("should open panel in an anchored state") {

                }
            }

            describe("data is updated and new data is propagated") {

            }

        }
    }
})
