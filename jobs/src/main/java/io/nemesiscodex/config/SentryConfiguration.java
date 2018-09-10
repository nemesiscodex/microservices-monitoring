package io.nemesiscodex.config;

import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SentryConfiguration {
    @Bean
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new io.sentry.spring.SentryExceptionResolver();
    }

    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new io.sentry.spring.SentryServletContextInitializer();
    }

    public SentryConfiguration(@Value("${sentry.dsn}") String sentryDsn) {
        Sentry.init(sentryDsn);
    }
}
