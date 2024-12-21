package ru.yandex.practicum.processor;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.AnalyzerConfig;
import ru.yandex.practicum.handlers.SnapshotHandler;
import ru.yandex.practicum.kafka.deserializer.SensorSnapshotDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
@RequiredArgsConstructor
public class SnapshotProcessor {

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler handler;
    private final AnalyzerConfig config;


    public void start() {
        try {
            consumer.subscribe(config.getHubTopics());

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer
                        .poll(config.getSnapshotConsumeAttemptTimeout());
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    SensorsSnapshotAvro sensorsSnapshotAvro = record.value();
                    log.info("Received snapshot from hub ID = {}", sensorsSnapshotAvro.getHubId());
                    handler.handle(sensorsSnapshotAvro);
                    manageOffsets(record, consumer);
                }
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Error:", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                consumer.close();
                log.info("Consumer close");
            }
        }
    }

    public void stop() {
        consumer.wakeup();
    }

    private static void manageOffsets(ConsumerRecord<String, SensorsSnapshotAvro> record,
                                      KafkaConsumer<String, SensorsSnapshotAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
    }
}
