package io.gitlab.arturbosch.detekt.rules.complexity

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.AbstractLongParameterList
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * Reports functions which have more parameters than a certain threshold (default: 6).
 *
 * @configuration threshold - number of parameters required to trigger the rule (default: `6`)
 * @configuration ignoreDefaultParameters - ignore parameters that have a default value (default: `false`)
 *
 * @active since v1.0.0
 */
class LongParameterList(
    config: Config = Config.empty,
    threshold: Int = DEFAULT_THRESHOLD_PARAMETER_LENGTH
) : AbstractLongParameterList(config, threshold) {

    override val issue = Issue("LongParameterList",
            Severity.Maintainability,
            "The more parameters a method has the more complex it is. Long parameter lists are often " +
                    "used to control complex algorithms and violate the Single Responsibility Principle. " +
                    "Prefer methods with short parameter lists.",
            Debt.TWENTY_MINS)

    override val ignoreDefaultParameters = valueOrDefault(IGNORE_DEFAULT_PARAMETERS, false)

    override fun visitNamedFunction(function: KtNamedFunction) {
        validateFunction(function, "The function ${function.nameAsSafeName}")
    }

    companion object {
        const val IGNORE_DEFAULT_PARAMETERS = "ignoreDefaultParameters"
        const val DEFAULT_THRESHOLD_PARAMETER_LENGTH = 6
    }
}
