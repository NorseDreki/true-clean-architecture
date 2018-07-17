package com.example.domain

import io.reactivex.Observable
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

class DummyComponentSpec : Spek({

    given("BS") {

        val c by memoized { println("comp");DummyComponent() }

        val sub by memoized {

            println("sub")
            val command = DummyComponent.Command.DATA("some")
            Observable.just(command).compose(c).test()
        }

        describe("client uses component as one-shot start command") {
            beforeEachTest {
                //c.justStart(DummyComponent.Command.DATA("some"))
                //Observable.just(command).compose(this).subscribe()
            }

            it("should receive processor results") {
                sub.assertValue(DummyComponent.Result.Empty)


            }

            it("should end processor subscription") {
                sub.assertComplete()


            }

            describe("render") {
                beforeEachTest {
                    println("perform")
                    val command = DummyComponent.Command.DATA("some")
                    c.justStart(command)
//                val testSub = c.render().test()
                }

                it("should not end view state subscription") {
                    val testSub = c.render().test()

                    //sub.assertSubscribed()
                                     testSub.assertNotComplete()

                }
            }

            describe("client emulates UI event and sends extra command") {
                beforeEachTest {
                    val command = DummyComponent.Command.DATA("some")
                    c.justStart(command)

                    c.sendCommand(command)
                }

                it("should process to update view state") {
                    val testSub = c.render().test()

                    testSub.assertValue(DummyComponent.ViewState())

                }
            }

            describe("client emulates screen lifecycle leave event") {
                it("should stop subscription to view state stream") {

                }

                it("should dispose 'upstream'") {

                }
            }
        }
    }
})