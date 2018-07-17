package com.example.domain

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

data class Depend(
        val component: DummyComponent,
        val results: Observable<DummyComponent.Result>,
        val scopeExit: PublishSubject<Unit>,
        val subsciption: TestObserver<DummyComponent.Result>
)

class DummyComponentSpec : Spek({


    given("BS") {

        val c by memoized { println("comp");DummyComponent() }

        val sub by memoized {

            println("sub")
            val command = DummyComponent.Command.DATA("some")
            Observable.just(command).compose(c)
        }

        val scoped by memoized {
            val scoped = PublishSubject.create<Unit>()
            sub.takeUntil(scoped)
            scoped
        }

        val deps by memoized {
            val command = DummyComponent.Command.DATA("some")

            val component = DummyComponent()
            val scopeExit = PublishSubject.create<Unit>()
            val results = Observable.just(command).compose(component)
            val subscription = results.firstOrError().test()//.takeUntil(scopeExit).test()

            Depend(
                    component,
                    results,
                    scopeExit,
                    subscription
            )
        }



        describe("client uses component as one-shot start command") {
            beforeEachTest {
                //c.justStart(DummyComponent.Command.DATA("some"))
                //Observable.just(command).compose(this).subscribe()
            }

            it("should receive processor results") {
                sub.test().assertValue(DummyComponent.Result.Empty)


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

                    testSub.assertOf {
                    }

                    testSub.assertValue(DummyComponent.ViewState())

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
                    val command = DummyComponent.Command.DATA("some")
                    deps.component.standalone(command)

                    deps.component.render().takeUntil(deps.scopeExit).subscribe()

                    deps.subsciption.dispose()

                    deps.scopeExit.onNext(Unit)


                }

                it("should not accept extra commands") {
                    val command = DummyComponent.Command.DATA("some")
                    assert(!deps.component.sendCommand(command))
                }

            }
        }
    }
})