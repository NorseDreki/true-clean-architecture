package com.example.domain.submitProposal2.fees

import com.example.domain.UiResult

sealed class FeesResult : UiResult {
    data class CalculatorLoaded(val calculator: FeesCalculator) : FeesResult()
    data class CalculatorRefreshFailed(val oldCalculator: FeesCalculator) : FeesResult()
}