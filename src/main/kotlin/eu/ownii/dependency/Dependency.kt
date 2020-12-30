package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

data class Dependency(
	internal val packages: List<GradlePackage>,
	internal val customs: List<DependencyHandlerScope.() -> Unit>
)
