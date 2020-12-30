plugins {
	`kotlin-dsl`
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
