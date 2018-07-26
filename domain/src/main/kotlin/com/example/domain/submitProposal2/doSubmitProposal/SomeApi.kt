package com.example.domain.submitProposal2.doSubmitProposal

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SomeApi : Api {
    override fun submitProposal(id: String, some: String): Observable<String> {
        return Observable.fromCallable {
            Thread.sleep(3000)
            "response"
        }.subscribeOn(Schedulers.io())
        //return Observable.error(IllegalStateException("sf"))
    }
}
