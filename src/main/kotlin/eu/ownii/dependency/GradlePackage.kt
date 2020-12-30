package eu.ownii.dependency

data class GradlePackage(
	internal val type: String,
	internal val group: String,
	internal val name: String,
	internal val version: String
)