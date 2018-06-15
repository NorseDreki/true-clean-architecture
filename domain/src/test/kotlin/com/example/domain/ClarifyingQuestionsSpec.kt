package com.example.domain

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class ClarifyingQuestionsSpec : Spek({

    context("some context") {
        val aaa = 1

        on("blah") {
            it("ah") {
                assert(aaa == 1) {
                    "bad"
                }

            }
        }
    }
})
