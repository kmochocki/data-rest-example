package com.kmochocki.datarestexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DataRestExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRestExampleApplication.class, args);
    }

}
