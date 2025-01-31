plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

group = "com.ll"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-websocket:3.4.1")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Database
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.security:spring-security-test")

	// Amazon S3 라이브러리
	implementation ("com.amazonaws:aws-java-sdk-s3:1.12.772")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
	annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	// ApiScheduler: Xml -> Json
	dependencies {
		implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.2")
		implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
		implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.2")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val querydslDir = "src/main/generated"

sourceSets {
	main {
		java {
			srcDir(querydslDir)
		}
	}
}

tasks.withType<JavaCompile> {
	options.generatedSourceOutputDirectory.set(file(querydslDir))
}

tasks.named("clean") {
	doLast {
		file(querydslDir).deleteRecursively()
	}
}
