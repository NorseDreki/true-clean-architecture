package com.example.domain.submitProposal2.fees

import com.example.domain.Processor
import com.example.domain.submitProposal2.fees.FeesCommands.LoadCalculator
import com.example.domain.submitProposal2.fees.FeesCommands.LoadUpdatedCalculator
import com.example.domain.submitProposal2.fees.FeesResult.CalculatorLoaded
import com.example.domain.submitProposal2.fees.FeesResult.CalculatorRefreshFailed
import io.reactivex.Observable

class FeesProcessor : Processor<FeesCommands, FeesResult> {

    override fun apply(upstream: Observable<FeesCommands>) =
            upstream.map {
                when (it) {
                    LoadCalculator -> {
                        var service: ScriptsService? = null

                        val script = service!!.getScript("1234")
                        val calc: FeesCalculator? = null

                        calc!!.evaluateFeesScript(script)

                        CalculatorLoaded(calc)
                    }
                    LoadUpdatedCalculator -> {
                        var service: ScriptsService? = null

                        val script = service!!.getScriptFromNetworkIgnoreCache("1234")
                        val calc: FeesCalculator? = null

                        calc!!.evaluateFeesScript(script)

                        CalculatorRefreshFailed(calc)
                    }
                }
            }!!
}
