package org.tinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.tinger"})
@SpringBootConfiguration
public class TingerSylvanasApplication {
    public static void main(String[] args) {
        SpringApplication.run(TingerSylvanasApplication.class, args);
    }
}
