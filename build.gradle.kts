import java.util.*

plugins {
	`kotlin-dsl`
	id("com.jfrog.bintray") version "1.8.5"
	id("maven-publish")
}

group = "eu.ownii"
version = "0.1"


repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
}

val artifactName = project.name
val artifactGroup = project.group.toString()
val artifactVersion = project.version.toString()

val pomUrl = "https://github.com/OwniiEU/DependencyDSL"
val pomScmUrl = "https://github.com/OwniiEU/DependencyDSL"
val pomIssueUrl = "https://github.com/OwniiEU/DependencyDSL/issues"
val pomDesc = "https://github.com/OwniiEU/DependencyDSL"

val githubRepo = "Ownii/DependencyDSL.git"
val githubReadme = "README.md"

val pomLicenseName = "The Apache License, Version 2.0"
val pomLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"

val pomDeveloperId = "Ownii"
val pomDeveloperName = "Martin Förster"

val sourcesJar by tasks.creating(Jar::class) {
	archiveClassifier.set("sources")
	from(sourceSets.getByName("main").allSource)
}

publishing {
	publications {
		create<MavenPublication>(artifactName) {
			groupId = artifactGroup
			artifactId = artifactName
			version = artifactVersion
			from(components["java"])

			artifact(sourcesJar)

			pom.withXml {
				asNode().apply {
					appendNode("description", pomDesc)
					appendNode("name", rootProject.name)
					appendNode("url", pomUrl)
					appendNode("licenses").appendNode("license").apply {
						appendNode("name", pomLicenseName)
						appendNode("url", pomLicenseUrl)
					}
					appendNode("developers").appendNode("developer").apply {
						appendNode("id", pomDeveloperId)
						appendNode("name", pomDeveloperName)
					}
					appendNode("scm").apply {
						appendNode("url", pomScmUrl)
					}
				}
			}
		}
	}
}

bintray {
	user = project.findProperty("bintrayUser").toString()
	key = project.findProperty("bintrayKey").toString()
	publish = true

	setPublications(artifactName)

	pkg.apply {
		repo = artifactName
		name = artifactName
		vcsUrl = pomScmUrl
		githubRepo = githubRepo
		desc = description
		websiteUrl = pomUrl
		issueTrackerUrl = pomIssueUrl
		githubReleaseNotesFile = githubReadme

		version.apply {
			name = artifactVersion
			desc = pomDesc
			released = Date().toString()
			vcsTag = artifactVersion
		}
	}

}
