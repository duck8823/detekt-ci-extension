package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.cli.console.BuildFailureReport

class CircleCIBuildFailureReport : ConsoleReport() {

    override val priority: Int = Int.MIN_VALUE

    override fun render(detektion: Detektion): String? = BuildFailureReport().render(detektion)
}
