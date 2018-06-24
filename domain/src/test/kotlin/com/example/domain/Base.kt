package com.example.domain

import io.reactivex.subjects.ReplaySubject
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given

fun <T: UiActor<*, *>> SpecBody.component(componentCreator: () -> T, body: Component<T>.() -> Unit) =
        Component(this, componentCreator).apply {
            given("a component ...") {
                body()
            }
        }

fun <T: UiActor<*, *>> Component<T>.initialized(cmd: UiCommand, body: InitializedComponent<T>.() -> Unit) =
        InitializedComponent(this.specBody, cmd, componentCreator).apply {
            specBody.context("component is initialized") {
                initialize()
                body()
            }
        }

open class Component<T : UiActor<*, *>>(
        val specBody: SpecBody,
        val componentCreator: () -> T
) {

    val component by specBody.memoized {
        println("memo")
        componentCreator()
    }
    open val cmdstr by specBody.memoized { ReplaySubject.create<UiCommand>() }
    val sub by specBody.memoized {
        (component as UiActor<UiCommand, UiResult>).process(cmdstr).test()
    }

    fun assertResultAt(index: Int, result: UiResult)
            = sub.assertValueAt(index, result)

}

class InitializedComponent<T : UiActor<*, *>>(
        val specBody1: SpecBody,
        val cmd: UiCommand,
        val componentCreator1: () -> T) : Component<T>(specBody1, componentCreator1) {

    fun initialize() {
        cmdstr.onNext(cmd)
    }

}
