package io.gitlab.arturbosch.detekt.rules.complexity

import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class LongConstructorParameterListSpec : Spek({

    val subject by memoized { LongConstructorParameterList() }

    describe("LongConstructorParameterList rule") {

        it("reports too long parameter list for primary constructors") {
            val code = "class LongCtor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int)"
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("does not report short parameter list for primary constructors") {
            val code = "class LongCtor(a: Int, b: Int, c: Int, d: Int, e: Int)"
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("reports too long parameter list for primary constructors event for parameters with defaults") {
            val code = "class LongCtor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int = 1)"
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("does not report long parameter list for primary constructors if parameters with defaults should be ignored") {
            val config = TestConfig(mapOf(LongParameterList.IGNORE_DEFAULT_PARAMETERS to "true"))
            val rule = LongConstructorParameterList(config)
            val code = "class LongCtor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int = 1, g: Int = 2)"
            assertThat(rule.compileAndLint(code)).isEmpty()
        }

        it("reports too long parameter list for secondary constructors") {
            val code = "class LongCtor() { constructor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int) }"
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("does not report short parameter list for primary constructors") {
            val code = "class LongCtor() { constructor(a: Int, b: Int, c: Int, d: Int, e: Int) }"
            assertThat(subject.compileAndLint(code)).isEmpty()
        }

        it("reports too long parameter list for secondary constructors event for parameters with defaults") {
            val code = "class LongCtor() { constructor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int = 1) }"
            assertThat(subject.compileAndLint(code)).hasSize(1)
        }

        it("does not report long parameter list for secondary constructors if parameters with defaults should be ignored") {
            val config = TestConfig(mapOf(LongParameterList.IGNORE_DEFAULT_PARAMETERS to "true"))
            val rule = LongParameterList(config)
            val code = "class LongCtor() { constructor(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int = 1, g: Int = 2) }"
            assertThat(rule.compileAndLint(code)).isEmpty()
        }
    }
})
