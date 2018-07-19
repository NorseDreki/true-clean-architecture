package com.example.domain

import com.example.domain.framework.asEmbedded
import com.example.domain.framework.asStandalone
import com.example.domain.framework.extraCommand
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFailsWith


class SampleComponentSpec : Spek({


    given("BS") {

        val command = SampleComponent.Command.DATA("some")

        val deps by memoized {
            println("deps")

            val command = SampleComponent.Command.DATA("some")

            val component
                    = SampleComponent().asStandalone(command).asEmbedded().as
            val scopeExit = PublishSubject.create<Unit>()
            //val results = Observable.just(command).compose(component)

            Depend2(
                    component,
              //      results,
                    scopeExit
            )
        }


        describe("client uses component as one-shot start command") {
            beforeEachTest {
                //deps.component.standalone(command)
                deps.component.viewStates().takeUntil(deps.scopeExit).test()


                deps.component.extraCommand(command)

                /*deps.component.sendCommand(command)
                deps.component.sendCommand(command)
                deps.component.sendCommand(command)

                deps.scopeExit.onNext(Unit)*/



                deps.scopeExit.onNext(Unit)


            }

            it("should have one emission for initial view state which corresponds to command") {

            }

            it("should not complete view state stream") {
                assertFailsWith<IllegalStateException> {
                    deps.component.extraCommand(command)
                }
            }

            describe("client issues an extra command while still sub") {
                perform {

                }

                it("should emit view state which corresponds to that command") {
                    //sub.assertValue
                }
            }

            describe("client ends subscription by leaving scope") {
                perform {

                }

                it("it should unsubscribe from commands subject") {

                }
            }

            describe("client subscribes for view states for second and other times") {
                perform {
                    println("another subscription")

                }

                it("should fail with illegal state exception") {
                    println("should fail")
                    assertFailsWith<IllegalStateException> {
                        deps.component.viewStates().test()
                    }
                }

            }

            describe("client sends crazy number of extra commands") {

            }



            it("") {
                //val sub = deps.component.render().test()

                //val sub = deps.component.onlyStates(command).test()

                //deps.component.render().test()
                //sub.assertValue(SampleComponent.ViewState())
            }
        }


        /*describe("client uses component as one-shot start command") {
            beforeEachTest {
                //c.justStart(SampleComponent.Command.DATA("some"))
                //Observable.just(command).compose(this).subscribe()
            }

            it("should receive processor results") {
                sub.test().assertValue(SampleComponent.Result.Empty)


            }

            describe("scope of component is exited") {
                beforeEachTest {
                    println("scoped")
                    //scoped.onNext(Unit)//.delay(100, TimeUnit.MILLISECONDS)
                    deps.component.render().takeUntil(deps.scopeExit).subscribe()
                    //deps.scopeExit.onNext(Unit)
                }

                it("should end processor subscription") {
                    deps.subsciption.assertComplete()
                }
            }

            describe("render") {
                beforeEachTest {
                    println("perform")
                    val command = SampleComponent.Command.DATA("some")
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
                    val command = SampleComponent.Command.DATA("some")
                    c.justStart(command)

                    c.sendCommand(command)
                }

                it("should process to update view state") {
                    val testSub = c.render().test()

                    testSub.assertOf {
                    }

                    testSub.assertValue(SampleComponent.ViewState())

                }
            }

            describe("client emulates screen lifecycle leave event") {
                it("should stop subscription to view state stream") {

                }

                it("should dispose 'upstream'") {

                }
            }

            describe("client unsubscribes from render by leaving scope") {
                beforeEachTest {
                    val command = SampleComponent.Command.DATA("some")
                    deps.component.standalone(command)
*//*
                    deps.component.sendCommand(command)
                    deps.component.sendCommand(command)
                    deps.component.sendCommand(command)
*//*

                    deps.component.render().takeUntil(deps.scopeExit).subscribe {
                        println("render $it 1")
                    }

                    deps.component.render().takeUntil(deps.scopeExit).subscribe {
                        println("render $it 2")
                    }

                    deps.component.render().takeUntil(deps.scopeExit).subscribe {
                        println("render $it 3")
                    }

                    deps.component.render().takeUntil(deps.scopeExit).subscribe {
                        println("render $it 4")
                    }

                    //deps.subsciption.dispose()

                    deps.scopeExit.onNext(Unit)


                }

                it("should not accept extra commands") {
                    val command = SampleComponent.Command.DATA("some")
                    assert(!deps.component.sendCommand(command))
                }

            }
        }*/
    }
})