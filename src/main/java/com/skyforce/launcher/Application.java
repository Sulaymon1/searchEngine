package com.skyforce.launcher;

import org.aspectj.lang.annotation.DeclarePrecedence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Sulaymon on 10.03.2018.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.skyforce")
@EnableJpaRepositories(basePackages = "com.skyforce.repositories")
@EnableAutoConfiguration
@EntityScan("com.skyforce.models")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }


}
