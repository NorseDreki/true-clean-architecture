package com.example.domain.submitProposal2.fees

interface ScriptsService {
    fun getScript(s: String): String
    fun getScriptFromNetworkIgnoreCache(s: String): String

}