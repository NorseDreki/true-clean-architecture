package com.example.domain.submitProposal2.doSubmitProposal

import io.reactivex.Observable

interface Api {
    fun submitProposal(id: String, some: String): Observable<String>
}