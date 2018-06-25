package com.example.domain.models

data class Proposal(
        val bid: Int,
        val questionAnswers: List<AnsweredQuestion>?,
        val coverLetter: String
)