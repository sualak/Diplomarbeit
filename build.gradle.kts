plugins {
	java
	id("org.springframework.boot") version "3.0.0"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "at.dertanzbogen"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}



dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation ("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.junit.jupiter:junit-jupiter:5.9.2")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.stripe:stripe-java:23.2.0")
	testImplementation("org.projectlombok:lombok:1.18.22")
	compileOnly("org.projectlombok:lombok:1.18.26")
	testCompileOnly("org.projectlombok:lombok:1.18.26")
	annotationProcessor("org.projectlombok:lombok:1.18.26")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.26")

	// https://mvnrepository.com/artifact/com.nulab-inc/zxcvbn
	implementation("com.nulab-inc:zxcvbn:1.7.0")
	//Fake data
	implementation ("com.github.javafaker:javafaker:1.0.2")
	// Crypto Bouncy Castle
	implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
	//API Testing
	testImplementation("io.rest-assured:rest-assured:5.3.0")
	testImplementation("com.jayway.jsonpath:json-path:2.7.0")
	//Map Struct
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
}

// TASKS ----------------------------------------------------------------------

// Tasks can be used to perform a wide variety of operations,
// such as compiling code, running tests, copying files, and deploying artifacts.

// Configures the `bootJar` task.
tasks.bootJar {
	// The `archiveFileName` specifies the name of the JAR file.
	// archiveFileName.set("shopping-portal.jar")
}

// Configures the `bootRun` task.
tasks.bootRun {
	// The `systemProperty` specifies the system property that is passed to the JVM when the application is run.
	// systemProperty("spring.profiles.active", "dev")
}

// Configures the `test` task.
tasks.test {
	// Use JUnit Platform for running tests
	useJUnitPlatform()
}

// Imports our custom tasks
apply(from = "fixtures-tasks.gradle.kts")
