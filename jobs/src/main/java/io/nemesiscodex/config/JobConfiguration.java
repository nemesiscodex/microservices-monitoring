package io.nemesiscodex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class JobConfiguration {

    @Bean
    public ExecutorService jobExecutor() {
        return Executors.newScheduledThreadPool(1);
    }
}
