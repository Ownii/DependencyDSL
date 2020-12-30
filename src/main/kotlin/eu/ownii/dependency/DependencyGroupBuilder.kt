package eu.ownii.dependency

import org.gradle.kotlin.dsl.DependencyHandlerScope

private const val IMPLEMENTATION = "implementation"
private const val TEST = "testImplementation"
private const val KAPT = "kapt"

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