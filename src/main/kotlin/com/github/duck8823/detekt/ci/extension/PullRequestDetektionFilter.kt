package com.github.duck8823.detekt.ci.extension

import io.gitlab.arturbosch.detekt.api.Detektion

interface PullRequestDetektionFilter {
    fun isPullRequest(): Boolean
    fun filter(detektion: Detektion): Detektion
}
