package com.example.domain

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertTrue


//@RunWith(JUnitPlatform::class)
object SomeSpec : Spek({

    given("Some") {
        val one by memoized { println("one"); "one"  }
        val two by memoized { println("two"); "two";  }

        context("context") {
            on("action") {
                println("action")
                it("it") {
                    one + two
                    assertTrue(true)
                }
            }
        }
    }

    /*given("A Foo foo") {
        val foo by memoized { Foo() }

        on("Change to foo") {
            foo.bar = "foo"

            it("Should have changed") {
                assertEquals("foo", foo.bar)
            }

            it("Should not have changed as well") {
                assertEquals("bar", foo.bar)
            }
        }

        on("No change") {
            it("Should not have changed") {
                assertEquals("bar", foo.bar)
            }
        }

    }*/

})
