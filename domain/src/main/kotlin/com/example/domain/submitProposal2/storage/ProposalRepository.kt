package com.example.domain.submitProposal2.storage

interface ProposalRepository {
    fun updateCoverLetter(id: String, coverLetter: String)

    fun updateQuestionAnswer()

    fun removeProposal()
}