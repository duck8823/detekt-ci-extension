package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.core.DetektResult
import org.eclipse.egit.github.core.RepositoryId
import org.eclipse.egit.github.core.service.PullRequestService
import org.jetbrains.kotlin.com.intellij.openapi.util.text.StringUtil

object TravisCIPullRequestDetektionFilter : PullRequestDetektionFilter {

    override fun isPullRequest(): Boolean = when {
        System.getenv("TRAVIS") != "true" -> false
        System.getenv("TRAVIS_PULL_REQUEST") == "false" -> false
        else -> true
    }

    override fun filter(detektion: Detektion): Detektion {
        val num = System.getenv("TRAVIS_PULL_REQUEST").toInt()

        val repo = StringUtil.split(System.getenv("TRAVIS_REPO_SLUG"), "/")
        val files = PullRequestService().getFiles(RepositoryId.create(repo.first(), repo.last()), num)

        val findings = detektion.findings.mapValues { (_, findings) ->
            findings.filter { finding ->
                files.any {
                    finding.file.contains(it.filename)
                }
            }
        }

        return DetektResult(findings)
    }
}
