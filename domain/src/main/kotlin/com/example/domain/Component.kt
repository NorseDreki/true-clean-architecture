package com.example.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

interface Component<C : UiCommand, R : UiResult, S : UiState> {

    val commands: PublishSubject<C>

    val processor: ObservableTransformer<C, R>

    val reducer: ObservableTransformer<R, S>

}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asStandalone(startingCommand: C) =
        StandaloneComponent(this, startingCommand)

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.asEmbedded() =
        EmbeddedComponent(this)


class StandaloneComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>,
        private val startingCommand: C
) :
        Component<C, R, S> by component,
        ViewStateProducer<S> {

    override fun viewStates(): Observable<S> {
        return Observable.just(startingCommand)
                .mergeWith(commands)
                .compose(processor)
                .compose(reducer)
        //or subject will be auto-disposed?
        //.doOnDispose { commands.onComplete() }
    }
}

class EmbeddedComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>
) :
        Component<C, R, S> by component,
        ObservableTransformer<C, R>,
        ViewStateProducer<S> {

    private lateinit var results: Observable<R>

    override fun apply(upstream: Observable<C>): Observable<R> {
        /*check(!::results.isInitialized) {
            "Can't compose component more than once"
        }*/

        val transformed = upstream.doOnComplete { println("upstream complete") }.doOnDispose { println("upstream dispose") }
                .mergeWith(commands.doOnComplete { println("subj completed") }.doOnDispose { println("subj disposed") })
                .compose(processor)
                .doOnComplete { println("complete") }
                .doOnDispose { println("dispose") }
                .doOnLifecycle({ println("onSub")}, { println("onDisp")})
                //.replay(2)
                .replay()
                .refCount()
                //.autoConnect(0)
                .doOnNext{ println("next $it")}
                .doOnComplete { println("complete3") }
                .doOnDispose { println("dispose3") }
                .doOnLifecycle({ println("onSub3")}, { println("onDisp3")})

        results = transformed/*.doOnDispose {
            println("dispose results")
            //commands.onComplete()
        }.doOnNext { println("res next") }*/
        println("set results")

        return transformed.doOnNext { println("trans $it") }
                .doOnDispose {
                    println("transf dispose results")
                    //commands.onComplete()
                }
                //.takeUntil(up.materialize().doOnNext { println("MAT $it") }.filter { it.isOnComplete })
                .doOnComplete { println("trans compl") }
    }

    override fun viewStates(): Observable<S> {
        checkNotNull(results) {
            "Render() must be called only after composing component"
        }

        //Thread.sleep(5000)

        return results.compose(reducer)
    }

}

fun <C : UiCommand, R : UiResult, S : UiState> Component<C, R, S>.extraCommand(command: C): Boolean {
    /*kotlin.check(::results.isInitialized) {
        "Can't compose component more than once"
    }*/


    //better throw exception
    kotlin.io.println("send command")

    //if (!commands.hasObservers())
    /*if (commands.hasComplete())
        return false*/

    kotlin.io.println("do send command")

    commands.onNext(command)

    return true
}

fun test() {
    //val c =
}
