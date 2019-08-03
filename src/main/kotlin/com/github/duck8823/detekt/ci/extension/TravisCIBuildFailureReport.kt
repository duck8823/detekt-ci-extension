package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.ConsoleReport
import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.cli.console.BuildFailureReport
import io.gitlab.arturbosch.detekt.core.DetektResult
import org.eclipse.egit.github.core.RepositoryId
import org.eclipse.egit.github.core.service.PullRequestService
import org.jetbrains.kotlin.com.intellij.openapi.util.text.StringUtil

class TravisCIBuildFailureReport : ConsoleReport() {
    private val buildFailureReport = BuildFailureReport()

    override val priority: Int = Int.MIN_VALUE

    override fun init(config: Config) {
        buildFailureReport.init(config)
    }

    override fun render(detektion: Detektion): String? {
        if (System.getenv("TRAVIS") != "true") {
            return buildFailureReport.render(detektion)
        }

        val num = System.getenv("TRAVIS_PULL_REQUEST")
        if (num == "false") {
            return buildFailureReport.render(detektion)
        }

        val repo = StringUtil.split(System.getenv("TRAVIS_REPO_SLUG"), "/")
        val files = PullRequestService().getFiles(RepositoryId.create(repo.first(), repo.last()), num.toInt())

        val findings = detektion.findings.mapValues { (_, findings) ->
            findings.filter { finding ->
                files.any {
                    finding.file.contains(it.filename)
                }
            }
        }

        return buildFailureReport.render(DetektResult(findings))
    }
}
