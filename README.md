# Kotlin Dependency DSL

- Group your dependencies that should stay together
- Implementation- & test-dependencies in one group
- Define one version for a whole group of dependencies
- Easy to read DSL

## Setup

Available via JCenter.

Add classpath to buildScript

```kotlin
buildscript {
	dependencies {
		classpath("eu.ownii:dependency-dsl:0.1")
	}
}
```

Add dependency to buildSrc/build.gradle.kts

```kotlin
dependencies {
	implementation("eu.ownii:dependency-dsl:0.1")
}
```

## Sample

```kotlin
// buildSrc/../Dependencies.kt
object Dependencies {
	val Ktor = dependency {
		group {
			version = "1.5.0"
			publisher = "io.ktor"
			packages = listOf(
				"ktor-server-core",
				"ktor-server-host-common",
				"ktor-server-netty",
				"ktor-gson"
			)
			test = listOf("ktor-server-tests")
			// kapt = "kapt-compiler-here"
		}

		group {
			version = "1.2.1"
			publisher = "ch.qos.logback"
			packages = listOf("logback-classic")
		}
	}
	val JodaTime = dependency {
		version = "2.10.6"
		publisher = "joda-time"
		packages = listOf("joda-time")
	}
	val JUnit = dependency {
		version = "5.3.1"
		publisher = "org.junit.jupiter"
		test = listOf(
			"junit-jupiter-api"
		)
		custom {
			"testRuntimeOnly"("$publisher:junit-jupiter-engine:$version")
		}
	}
}

// app/build.gradle.kts

dependencies {
	with(Dependencies) {
		use(Ktor)
		use(JodaTime)
		use(JUnit)
	}
}
```