package com.example.domain

import io.reactivex.observers.TestObserver
import io.reactivex.subjects.ReplaySubject
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.context


@TestDsl
fun <T: UiActor<*, *>> SpecBody.component(componentCreator: () -> T, body: Component<T>.() -> Unit) =
        Component(this, componentCreator).apply {
            context("a component ...") {
                body()
            }
        }

@TestDsl
fun <T: UiActor<*, *>> Component<T>.initialized(cmd: UiCommand, body: InitializedComponent<T>.() -> Unit) =
        InitializedComponent(this.specBody, cmd, componentCreator).apply(body)

data class Deps(val cmdstr: ReplaySubject<UiCommand>, val sub: TestObserver<UiResult>)

@TestDsl
open class Component<T : UiActor<*, *>>(
        val specBody: SpecBody,
        val componentCreator: () -> T
) {

    /*val component by specBody.memoized {
        println("memo")
        componentCreator()
    }*/

    open val deps by specBody.memoized {
        println("deps")
        val cmdstr =
            ReplaySubject.create<UiCommand>()
        val sub =
            (componentCreator() as UiActor<UiCommand, UiResult>).process(cmdstr).test()
        Deps(cmdstr, sub)
    }

    /*val cmdstr by specBody.memoized {
        println("cmdstr")
        ReplaySubject.create<UiCommand>() }
    val sub by specBody.memoized {
        println("sub")
        (componentCreator() as UiActor<UiCommand, UiResult>).process(cmdstr).test()
    }*/

    fun command(command: UiCommand) {
        deps.cmdstr.onNext(command)
    }

    fun assertResultAt(index: Int, result: UiResult): TestObserver<UiResult> {
        println("assert $result")
        return deps.sub.assertValueAt(index, result)

    }

}

@TestDsl
class InitializedComponent<T : UiActor<*, *>>(
        val specBody1: SpecBody,
        val cmd: UiCommand,
        val componentCreator1: () -> T) : Component<T>(specBody1, componentCreator1) {

    override val deps by specBody1.memoized {
        val d = super.deps
        d.cmdstr.onNext(cmd)
        //initialize()
        d
    }

    fun initialize() {
        println("initialize")
        deps.cmdstr.onNext(cmd)
    }

}
