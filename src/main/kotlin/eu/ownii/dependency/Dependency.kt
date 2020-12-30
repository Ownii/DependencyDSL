package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

data class Dependency(val packages: List<GradlePackage>, val customs: List<DependencyHandlerScope.() -> Unit>)
