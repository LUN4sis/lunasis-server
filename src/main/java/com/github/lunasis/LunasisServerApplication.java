package com.github.lunasis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LunasisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LunasisServerApplication.class, args);
    }

}
