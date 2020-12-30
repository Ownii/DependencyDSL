package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope


fun dependency(action: DependencyBuilder.() -> Unit): Dependency {
	val builder = DependencyBuilder()
	action(builder)
	val (packages, customs) = builder.build()
	return Dependency(packages, customs)
}

@DependencyTagMarker
class DependencyBuilder(
	version: String? = null,
	publisher: String? = null,
	packages: List<String>? = null,
	test: List<String>? = null,
	kapt: String? = null
) : DependencyGroupBuilder(version, publisher, packages, test, kapt) {

	private val groupBuilders = mutableListOf<DependencyGroupBuilder>()

	fun group(action: DependencyGroupBuilder.() -> Unit) {
		val builder = DependencyGroupBuilder()
		builder.action()
		groupBuilders.add(builder)
	}

	override fun build(): Pair<List<GradlePackage>, List<DependencyHandlerScope.() -> Unit>> {
		val (packages, customs) = super.build()
		val allPackages = packages.toMutableList()
		val allCustoms = customs.toMutableList()
		groupBuilders.forEach { group ->
			val (groupPackages, groupCustoms) = group.build()
			allPackages.addAll(groupPackages)
			allCustoms.addAll(groupCustoms)
		}
		return Pair(allPackages, allCustoms)
	}
}

