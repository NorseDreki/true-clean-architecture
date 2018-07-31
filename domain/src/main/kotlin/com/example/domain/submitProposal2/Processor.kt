package com.example.domain.submitProposal2

import com.example.domain.UiCommand
import com.example.domain.UiResult
import com.example.domain.framework.WithLoopback
import com.example.domain.framework.WithProcessors
import com.example.domain.framework.WithResults
import com.example.domain.submitProposal2.clarifyingQuestions.ClarifyingQuestions
import com.example.domain.submitProposal2.coverLetter.CoverLetter
import com.example.domain.submitProposal2.doSubmitProposal.DoSubmitProposal
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class Processor(
        val asActor: ObservableTransformer<CoverLetter.Command, CoverLetter.Result>,
        val asActor1: ObservableTransformer<ClarifyingQuestions.Command, ClarifyingQuestions.Result>,
        val asActor2: ObservableTransformer<DoSubmitProposal.Command, DoSubmitProposal.Result>
) : ObservableTransformer<SubmitProposal.Command, UiResult> {


    val inner =
            ObservableTransformer<UiCommand, UiResult> {
                it
                        .compose(WithProcessors(
                                SubmitProposal.Command::class.java as Class<Any> to storageLoader as ObservableTransformer<Any, UiResult>,
                                SubmitProposal.Command.ToNextStep::class.java as Class<Any> to navigationProcessor as ObservableTransformer<Any, UiResult>,
                                CoverLetter.Command::class.java as Class<Any> to asActor as ObservableTransformer<Any, UiResult>,
                                ClarifyingQuestions.Command::class.java as Class<Any> to asActor1 as ObservableTransformer<Any, UiResult>,
                                DoSubmitProposal.Command::class.java as Class<Any> to asActor2 as ObservableTransformer<Any, UiResult>
                        ))
                        .compose(WithResults<UiResult>(
                                submitAllowedProcessor as ObservableTransformer<UiResult, UiResult>,
                                storageSaver
                        ))
                        .compose(WithResults<UiResult>(
                                PublishedResults() as ObservableTransformer<UiResult, UiResult>
                        ))
            }


    override fun apply(upstream: Observable<SubmitProposal.Command>) =
        upstream
                .compose(WithLoopback(inner, LoopbackCommands()))!!
}
