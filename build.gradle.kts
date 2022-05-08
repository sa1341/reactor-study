import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.moowork.gradle.node.yarn.YarnTask

plugins {
	id("org.springframework.boot") version "2.5.12"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.github.node-gradle.node") version "2.2.4"
	kotlin("jvm") version "1.5.32"
	kotlin("plugin.spring") version "1.5.32"
}

group = "com.reactive"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	//implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation ("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	runtimeOnly("dev.miku:r2dbc-mysql")
	runtimeOnly("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

node{
	version = "16.13.0"
	distBaseUrl = "https://nodejs.org/dist"

	download = System.getenv("REQUIRE_NODE_INSTALL") != null && System.getenv("REQUIRE_NODE_INSTALL") == "TRUE"
	workDir = file("${project.buildDir}/nodejs")
	yarnWorkDir = file("${project.buildDir}/yarn")
	nodeModulesDir = file("${project.projectDir}")
	npmWorkDir = file("${project.buildDir}/npm")
}


val frontProjectDir = "${project.projectDir}/src/main/react-yolo-client"
val frontOutputDir = "${frontProjectDir}/dist/spa"
val frontendNpmRunStage = "build"

val installDependencies by tasks.registering(YarnTask::class) {
	args = listOf("install")
	println("$frontProjectDir")
	setExecOverrides(closureOf<ExecSpec> {
		setWorkingDir("$frontProjectDir")
	})
}

val reactInstallDependencies by tasks.registering(YarnTask::class) {
	dependsOn(installDependencies)
	args = listOf("add", "react-scripts")
	println("$frontProjectDir")
	setExecOverrides(closureOf<ExecSpec> {
		setWorkingDir("$frontProjectDir")
	})
}

val buildReactTask by tasks.registering(YarnTask::class) {
	// Before buildWeb can run, installDependencies must run
	dependsOn(reactInstallDependencies)
	args = listOf("run", frontendNpmRunStage)
	setExecOverrides(closureOf<ExecSpec> {
		setWorkingDir("$frontProjectDir")
	})
}

val copyTask by tasks.registering(Copy::class) {
	dependsOn(buildReactTask)
	from(file("$frontProjectDir/build"))
	into(file("${project.buildDir}/resources/main/static"))
}

tasks.jar {
	enabled = false
}

tasks.bootJar {
	dependsOn(copyTask)
}
