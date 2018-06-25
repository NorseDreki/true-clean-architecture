package com.example.domain.models

data class ItemDetails(
        val id: String,
        val isCoverLetterRequired: Boolean,
        val questions: List<Question>?
)