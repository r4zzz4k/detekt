package io.gitlab.arturbosch.detekt.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Metric
import io.gitlab.arturbosch.detekt.api.ThresholdRule
import io.gitlab.arturbosch.detekt.api.ThresholdedCodeSmell
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtParameterList

abstract class AbstractLongParameterList(
    config: Config,
    threshold: Int
) : ThresholdRule(config, threshold) {

    abstract val ignoreDefaultParameters: Boolean

    protected fun validateFunction(function: KtFunction, label: String) {
        if (function.isOverride()) return
        val parameterList = function.valueParameterList
        val parameters = parameterList?.parameterCount(ignoreDefaultParameters)

        if (parameters != null && parameters >= threshold) {
            report(ThresholdedCodeSmell(issue,
                    Entity.from(parameterList),
                    Metric("SIZE", parameters, threshold),
                    "$label has too many parameters. The current threshold" +
                            " is set to $threshold."))
        }
    }

    private fun KtParameterList.parameterCount(ignoreDefaultParameters: Boolean): Int {
        return if (ignoreDefaultParameters) {
            parameters.filter { !it.hasDefaultValue() }.size
        } else {
            parameters.size
        }
    }
}
