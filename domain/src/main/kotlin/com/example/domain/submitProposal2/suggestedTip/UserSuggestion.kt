package com.example.domain.submitProposal2.suggestedTip

interface UserSuggestion {

    fun getSuggestionForUser(): Int

    fun hasSuggestionAvailable(): Boolean
}