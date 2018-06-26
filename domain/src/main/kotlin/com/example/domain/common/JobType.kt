package com.example.domain.common


import com.example.domain.UiResult
import com.example.domain.UiState
import io.reactivex.ObservableTransformer

data class JobTypeViewState(
        val isHourly: Boolean,
        val skillLevel: String?,
        val engagementDuration: String?,
        val fixedPriceBudget: String?
) : UiState {
    companion object {
        fun init() = JobTypeViewState(false, "", "", "")
    }
}

val jobTypeReducer =
        ObservableTransformer<UiResult, UiState> {
            it.scan<UiState>(JobTypeViewState.init()) { state, result ->
                when (result) {
                    /*is JobResult.Data -> {
                        JobTypeViewState(
                                result.job.jobType.rawValue!!.equals("hourly", ignoreCase = true),
                                result.job.experienceLevel.displayValue,
                                result.job.estimatedDuration.displayValue,
                                result.job.fixedPriceBudget.displayValue
                        )
                    }*/
                    else -> throw IllegalStateException("sdf")
                }
            }
        }
