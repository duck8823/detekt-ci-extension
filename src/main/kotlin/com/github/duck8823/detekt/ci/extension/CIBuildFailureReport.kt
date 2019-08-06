package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.cli.console.BuildFailureReport

class CIBuildFailureReport : ConsoleReport() {

    private val buildFailureReport = BuildFailureReport()

    override val priority: Int = Int.MIN_VALUE

    override fun init(config: Config) {
        buildFailureReport.init(config)
    }

    override fun render(detektion: Detektion): String? {
        val filter = PullRequestDetektionFilter::class.nestedClasses.mapNotNull {
            it.objectInstance as PullRequestDetektionFilter?
        }.find { it.isPullRequest() }

        return buildFailureReport.render(filter?.filter(detektion) ?: detektion)
    }
}
