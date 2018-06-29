package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.submitProposal.ProposeTerms
import com.example.domain.submitProposal.ProposeTerms.Command.DATA
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

//@RunWith(JUnitPlatform::class)
class ProposeTermsSpec : Spek({

    val withNotEnteredBid = ItemOpportunity(
            ItemDetails("1234", false, null),
            Proposal(0, null, "")
    )

    component(::ProposeTerms) {

        initialized(DATA(withNotEnteredBid)) {

            describe("client initializes component with data with not entered bid") {

                it("should have message initialized anyways") {
                    assertResultAt(0, ProposeTerms.Result.ItemGreetingLoaded("greeting"))
                }

                it("should emit empty bid result with no fee calculated") {
                    assertResultAt(1, ProposeTerms.Result.BidEmpty)
                }

                it("should emit result as engagement not required") {
                    assertResultAt(2, ProposeTerms.Result.EngagementNotRequired)
                }
            }

            describe("client sends data with updated fee calculator") {
                it("should recalculate fee") {

                }
            }

        }
    }
})
