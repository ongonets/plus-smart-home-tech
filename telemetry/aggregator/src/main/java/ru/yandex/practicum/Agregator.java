package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Agregator {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Agregator.class, args);
        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
        Runtime.getRuntime().addShutdownHook(new Thread(aggregator::stop));
        aggregator.start();
    }
}
