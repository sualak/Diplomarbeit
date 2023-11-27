package at.dertanzbogen.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// What is @ConfigurationProperties
// ---------------------------------
// @ConfigurationProperties is an annotation used in Spring Boot applications
// to bind external configuration properties (e.g., from application.properties) to a Java class.
// This annotation provides a convenient way to access and manage configuration properties using
// strongly typed classes, allowing you to benefit from features such as autocompletion,
// validation, and type-safety in your code.

// For example, the following application.properties file will be bound to the FixtureConfig class:
// fixtures.path=src/main/resources/fixtures

@Configuration
@ConfigurationProperties(prefix = "fixtures")
@Getter
@Setter
public class TestFixturesProperties
{
    private String path;
}
