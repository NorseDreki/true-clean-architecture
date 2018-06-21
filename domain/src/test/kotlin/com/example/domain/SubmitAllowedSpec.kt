package com.example.domain

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class SubmitAllowedSpec : Spek({

    given("a SubmitAllowed thunk") {
        context("item details has no questions") {

            it("should not fail on non-target events") {

            }
            it ("should not emit on non-target events") {

            }
        }
    }
}