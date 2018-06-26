package com.example.domain.models

data class ItemDetails(
        val id: String,
        val isCoverLetterRequired: Boolean,
        /*val minBid: Int,
        val maxBid,*/
        val questions: List<Question>?
)