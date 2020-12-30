package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

private const val IMPLEMENTATION = "implementation"
private const val TEST = "testImplementation"
private const val KAPT = "kapt"

@DslMarker
annotation class DependencyTagMarker

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

@DependencyTagMarker
open class DependencyGroupBuilder(
	var version: String? = null,
	var publisher: String? = null,
	var packages: List<String>? = null,
	var test: List<String>? = null,
	var kapt: String? = null
) {

	var custom: (DependencyHandlerScope.() -> Unit)? = null

	internal open fun build(): Pair<List<GradlePackage>, List<DependencyHandlerScope.() -> Unit>> {
		val allPackages = mutableListOf<GradlePackage>()

		allPackages.addPackages(IMPLEMENTATION, packages)
		allPackages.addPackages(TEST, test)
		allPackages.addPackages(KAPT, kapt?.let { listOf(kapt!!) })
		return Pair(allPackages, listOfNotNull(custom))
	}

	private fun MutableList<GradlePackage>.addPackages(type: String, packages: List<String>?) {
		packages?.forEach { pkg ->
			this.add(
				GradlePackage(
					type = type,
					group = publisher.orEmpty(),
					name = pkg,
					version = version.orEmpty()
				)
			)
		}
	}

	fun custom(action: DependencyHandlerScope.() -> Unit) {
		custom = action
	}
}

data class Dependency(val packages: List<GradlePackage>, val customs: List<DependencyHandlerScope.() -> Unit>)

data class GradlePackage(
	val type: String,
	val group: String,
	val name: String,
	val version: String
)

infix fun DependencyHandlerScope.use(dependency: Dependency) {
	dependency.packages.forEach { pkg ->
		val name = "${pkg.group}:${pkg.name}:${pkg.version}".trim(':')
		add(pkg.type, name)
	}
	dependency.customs.forEach { action ->
		action()
	}
}