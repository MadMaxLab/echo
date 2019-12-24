package io.github.madmaxlab.echo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class EchoWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EchoWebApplication.class, args);
    }

}
