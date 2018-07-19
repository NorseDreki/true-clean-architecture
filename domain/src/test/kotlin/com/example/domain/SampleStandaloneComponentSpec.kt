package com.example.domain

import com.example.domain.framework.asStandalone
import com.example.domain.framework.extraCommand
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse


class SampleStandaloneComponentSpec : Spek({

    given("BS") {
        val command = SampleComponent.Command.DATA("some")

        val deps by memoized {
            println("deps")

            val component
                    = SampleComponent().asStandalone(command)
            val scopeExit = PublishSubject.create<Unit>()

            val subscription
                    = component.viewStates().takeUntil(scopeExit).test()

            Depend2(
                    component,
                    scopeExit,
                    subscription
            )
        }

        describe("client uses component as one-shot start command") {
            it("should have one emission for initial view state which corresponds to command") {
                deps.subscription.assertValueAt(0, SampleComponent.ViewState())
            }

            describe("client issues extra command after view state subscription is gone") {
                perform {
                    deps.scopeExit.onNext(Unit)
                }

                it("should fail with illegal state exception") {
                    assertFailsWith<IllegalStateException> {
                        deps.component.extraCommand(command)
                    }
                }
            }

            describe("client issues an extra command while still sub") {
                perform {
                    deps.component.extraCommand(command)
                }

                it("should emit view state which corresponds to that command") {
                    deps.subscription.assertValueAt(1, SampleComponent.ViewState())
                }

                it("should emit two values only") {
                    deps.subscription.assertValueCount(2)
                }

                it("should not complete view states stream") {
                    deps.subscription.assertNotTerminated()
                }
            }

            describe("client ends subscription by leaving scope") {
                perform {
                    deps.scopeExit.onNext(Unit)
                }

                it("it should unsubscribe from commands subject") {
                    assertFalse { deps.component.commands.hasObservers() }
                }
            }

            describe("client subscribes for view states for second and other times") {
                it("should fail with illegal state exception") {
                    assertFailsWith<IllegalStateException> {
                        deps.component.viewStates().test()
                    }
                }
            }

            describe("client sends crazy number of extra commands") {
                perform {
                    (1..1000).forEach {
                        deps.component.extraCommand(command)
                    }
                }

                it("") {
                    (1..1000).forEach {
                        deps.subscription.assertValueAt(it, SampleComponent.ViewState())
                    }
                }
            }
        }
    }
})
