package com.example.domain.submitProposal

import com.example.domain.UiCommand
import com.example.domain.UiResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import java.util.concurrent.TimeUnit

sealed class DoSubmitProposalCommand : UiCommand {
    data class DoSubmit(val id: String) : DoSubmitProposalCommand()
}

sealed class DoSubmitProposalResult : UiResult {
    object InProgress : DoSubmitProposalResult()
    //    object ProposalRemoved : DoSubmitProposalResult()
    data class Success(val response: String) : DoSubmitProposalResult()
    data class Error(val exception: Throwable) : DoSubmitProposalResult()
}

//handle job updates to update job title in Proposal, and questions
//alert if both changed?

interface Api {
    fun submitProposal(id: String, some: String): Observable<String>
}

class SomeApi : Api {
    override fun submitProposal(id: String, some: String): Observable<String> {
        return Observable.just("response")
    }

}

val doSubmitProposalProcessor =
        ObservableTransformer<DoSubmitProposalCommand, UiResult> {
            it.flatMap {
                when (it) {
                    is DoSubmitProposalCommand.DoSubmit -> {
                        val api: Api? = SomeApi()

                        api!!.submitProposal("123", "dsf")
                                .delay(3, TimeUnit.SECONDS)
                                .map(DoSubmitProposalResult::Success)
                                .cast(UiResult::class.java)
                                .onErrorReturn(DoSubmitProposalResult::Error)
                                .startWith(DoSubmitProposalResult.InProgress)
                    }
                }
            }
        }
