package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.cli.console.BuildFailureReport

class CircleCIBuildFailureReport : ConsoleReport() {

    private val buildFailureReport = BuildFailureReport()

    override val priority: Int = Int.MIN_VALUE

    override fun init(config: Config) {
        buildFailureReport.init(config)
    }

    override fun render(detektion: Detektion): String? = buildFailureReport.render(detektion)
}
