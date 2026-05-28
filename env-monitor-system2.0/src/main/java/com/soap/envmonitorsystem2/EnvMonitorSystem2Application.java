package com.soap.envmonitorsystem2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnvMonitorSystem2Application {

    public static void main(String[] args) {
        SpringApplication.run(EnvMonitorSystem2Application.class, args);
    }
}
