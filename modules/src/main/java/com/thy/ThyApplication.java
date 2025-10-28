package com.thy;

import com.thy.repository.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class ThyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThyApplication.class, args);
    }
}
