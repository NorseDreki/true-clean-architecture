package com.example.domain.framework

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.UiState
import com.example.domain.ViewStateProducer
import com.example.domain.submitProposal2.doSubmitProposal.proposalConfirmation.ProposalConfirmation
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

class StandaloneComponent<C : UiCommand, R : UiResult, S : UiState>
internal constructor(
        private val component: Component<C, R, S>,
        private val startingCommand: C
) :
        Component<C, R, S> by component,
        ComponentImplementation,
        ViewStateProducer<S> {

    private val alreadyUsed = AtomicBoolean(false)

    override fun viewStates() =
            if (alreadyUsed.compareAndSet(false, true)) {
                commands = PublishSubject.create()

                Observable.just(startingCommand)
                        .doOnNext { println("next st") }
                        .doOnDispose { println("dispose st") }
                        .doOnComplete { println("complete st") }
                        .mergeWith(commands
                                .doOnNext {
                                    if (it is ProposalConfirmation.Command.Dismiss) {
                                        println("completing commands")
                                        commands.onComplete()
                                    }
                                }
                                .doOnDispose { println("dispose cmd") }
                                .doOnComplete { println("complete cmd") })
                        .compose(processor)
                        .compose(reducer)
                        .doOnNext { println("next $it") }
                        .doOnDispose { println("dispose") }
                        .doOnComplete { println("complete") }!!
            } else {
                throw IllegalStateException("Must not be used more than once")
            }
}
