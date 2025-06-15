package de.training.wetterkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class WetterKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WetterKafkaApplication.class, args);
    }

}
