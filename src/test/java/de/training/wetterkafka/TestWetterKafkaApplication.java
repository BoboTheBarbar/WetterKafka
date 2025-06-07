package de.training.wetterkafka;

import org.springframework.boot.SpringApplication;

public class TestWetterKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.from(WetterKafkaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
