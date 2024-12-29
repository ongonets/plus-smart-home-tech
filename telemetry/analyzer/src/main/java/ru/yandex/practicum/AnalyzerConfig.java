package ru.yandex.practicum;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Configuration
@ConfigurationProperties("analyzer")
@Getter
@Setter
public class AnalyzerConfig {

    private List<String> hubTopics;
    private Duration hubConsumeAttemptTimeout;
    private Properties hubConsumerProperties;
    private List<String> snapshotTopics;
    private Duration snapshotConsumeAttemptTimeout;
    private Properties snapshotConsumerProperties;


    @Bean
    public KafkaConsumer<String, HubEventAvro> hubConsumer() throws Exception {
        return new KafkaConsumer<>(getHubConsumerProperties());
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> consumer() throws Exception {
        return new KafkaConsumer<>(getSnapshotConsumerProperties());
    }

}
