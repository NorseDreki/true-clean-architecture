package com.example.domain

import com.example.domain.models.ItemDetails
import com.example.domain.models.ItemOpportunity
import com.example.domain.models.Proposal
import com.example.domain.submitProposal.ClarifyingQuestions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

//@RunWith(JUnitPlatform::class)
class ClarifyingQuestions2Spec : Spek({

    val itemOpportunity = ItemOpportunity(
            ItemDetails("1234", null),
            Proposal(0, null)
    )

    component(::ClarifyingQuestions) {
        initialized(ClarifyingQuestions.Command.INIT(itemOpportunity)) {

            on("init") {
                it("should") {
                    assertResultAt(0, ClarifyingQuestions.Result.NoQuestions)
                }
            }
        }
    }
})