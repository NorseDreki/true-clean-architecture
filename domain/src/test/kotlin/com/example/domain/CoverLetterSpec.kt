package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.submitProposal.CoverLetter
import com.example.domain.submitProposal.CoverLetter.Command.DATA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.xit
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class CoverLetterSpec : Spek({

    val withEmptyCoverLetter = ItemOpportunity(
            ItemDetails("1234", true, null),
            Proposal(0, null, "")
    )

    val withCoverLetterNotRequired = ItemOpportunity(
            ItemDetails("1234", false, null),
            Proposal(0, null, "")
    )

    component(::CoverLetter) {

        initialized(DATA(withEmptyCoverLetter)) {

            describe("client sends DATA with cover letter required") {
                xit("should emit status as not completed") {

                }

                it("should emit empty cover letter") {
                    assertResultAt(0, CoverLetter.Result.Empty)

                }
            }

            describe("client fills in a valid cover letter") {
                perform {
                    command(CoverLetter.Command.UpdateCoverLetter("cover letter"))
                }

                it("should emit valid cover letter") {
                    assertResultAt(1, CoverLetter.Result.Valid("cover letter"))
                }
            }
        }

        initialized(DATA(withCoverLetterNotRequired)) {

            describe("client sends DATA with cover letter not required") {
                it("should emit only 'not required' result") {
                    assertValuesOnly(CoverLetter.Result.NoCoverLetterRequired)
                }
            }

            describe("client tries to enter cover letter when it's not required") {
                xit("should fail with error") {

                }
            }
        }
    }
})
