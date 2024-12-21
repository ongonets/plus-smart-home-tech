package ru.yandex.practicum;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AggregationStarter {

    private static final int COUNT_COMMIT_OFFSETS = 10;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private final RecordHandler recordHandler;
    private final AggregatorConfig aggregatorConfig;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;


    public void start() {
        try {
            consumer.subscribe(aggregatorConfig.getSensorTopic());

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer
                        .poll(aggregatorConfig.getConsumeAttemptTimeout());

                int count = 0;
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> sensorsSnapshotAvroOpt = recordHandler.updateState(record.value());
                    if (sensorsSnapshotAvroOpt.isPresent()) {
                        SensorsSnapshotAvro snapshotAvro = sensorsSnapshotAvroOpt.get();
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(aggregatorConfig.getSnapshotTopic(),
                                        null,
                                        snapshotAvro.getTimestamp().getEpochSecond(),
                                        null,
                                        snapshotAvro);
                        producer.send(producerRecord);
                        log.info("Snapshot from hub ID = {} send to topic: {}", snapshotAvro.getHubId(),
                                aggregatorConfig.getSnapshotTopic());
                    }
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Error sensor events processing:", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync(currentOffsets);
            } finally {
                consumer.close();
                log.info("Consumer closed");
                producer.close();
                log.info("Producer closed");
            }
        }
    }

    public void stop() {
        consumer.wakeup();
    }

    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count,
                                      KafkaConsumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % COUNT_COMMIT_OFFSETS == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Commit offsets error: {}", offsets, exception);
                }
            });
        }
    }
}