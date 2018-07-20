package com.example.domain.submitProposal2.doSubmitProposal

import io.reactivex.Observable

class SomeApi : Api {
    override fun submitProposal(id: String, some: String): Observable<String> {
        return Observable.just("response")
        //return Observable.error(IllegalStateException("sf"))
    }

}