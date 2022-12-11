package org.tinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.tinger"})
@SpringBootConfiguration
//@EnableOpenApi
public class TingerTarzanApplication {
    public static void main(String[] args) {
        SpringApplication.run(TingerTarzanApplication.class, args);
    }
}
