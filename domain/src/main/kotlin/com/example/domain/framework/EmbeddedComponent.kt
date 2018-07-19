package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.ViewStateProducer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class EmbeddedComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>
) :
        Component<C, R, S> by component,
        ComponentImplementation,
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
                .doOnLifecycle({ println("onSub") }, { println("onDisp") })
                .replay()
                .refCount()
                .doOnNext { println("next $it") }
                .doOnComplete { println("complete3") }
                .doOnLifecycle({ println("onSub3") }, { println("onDisp3") })

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