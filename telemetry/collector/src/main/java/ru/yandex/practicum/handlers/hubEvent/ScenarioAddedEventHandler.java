package ru.yandex.practicum.handlers.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.handlers.TimestampMapper;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.kafkaClient.KafkaClient;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddedEventHandler implements HubEventHandler {

    @Value(value = "${hubEventTopic}")
    private String topic;

    private final KafkaClient kafkaClient;


    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto eventProto) {
        HubEventAvro eventAvro = map(eventProto);
        ProducerRecord<String, SpecificRecordBase> producerRecord = new ProducerRecord<>(topic, null,
                eventAvro.getTimestamp().getEpochSecond(), null, eventAvro);
        kafkaClient.getProducer().send(producerRecord);
        log.info("ScenarioAddedEvent from hub ID = {} send to topic: {}", eventAvro.getHubId(), topic);
    }

    private HubEventAvro map(HubEventProto eventProto) {
        ScenarioAddedEventProto scenarioAddedEventProto = eventProto.getScenarioAdded();
        List<DeviceActionAvro> deviceActionAvroList = scenarioAddedEventProto.getActionList().stream()
                .map(this::map)
                .toList();
        List<ScenarioConditionAvro> scenarioConditionAvroList = scenarioAddedEventProto.getConditionList().stream()
                .map(this::map)
                .toList();
        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setActions(deviceActionAvroList)
                .setConditions(scenarioConditionAvroList)
                .build();
        return HubEventAvro.newBuilder()
                .setHubId(eventProto.getHubId())
                .setTimestamp(TimestampMapper.mapToInstant(eventProto.getTimestamp()))
                .setPayload(scenarioAddedEventAvro)
                .build();
    }

    private ScenarioConditionAvro map(ScenarioConditionProto conditionProto) {
        Object value = null;
        switch (conditionProto.getValueCase()) {
            case INT_VALUE -> value = conditionProto.getIntValue();
            case BOOL_VALUE -> value = conditionProto.getBoolValue();
        }
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(conditionProto.getSensorId())
                .setType(ConditionTypeAvro.valueOf(conditionProto.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(conditionProto.getOperation().name()))
                .setValue(value)
                .build();
    }

    private DeviceActionAvro map(DeviceActionProto actionProto) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(actionProto.getSensorId())
                .setValue(actionProto.getValue())
                .setType(ActionTypeAvro.valueOf(actionProto.getType().name()))
                .build();
    }
}

