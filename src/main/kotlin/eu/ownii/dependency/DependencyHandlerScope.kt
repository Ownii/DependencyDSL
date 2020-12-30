package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

infix fun DependencyHandlerScope.use(dependency: Dependency) {
	dependency.packages.forEach { pkg ->
		val name = "${pkg.group}:${pkg.name}:${pkg.version}".trim(':')
		add(pkg.type, name)
	}
	dependency.customs.forEach { action ->
		action()
	}
}