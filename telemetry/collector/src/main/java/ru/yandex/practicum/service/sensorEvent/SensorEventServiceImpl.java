package ru.yandex.practicum.service.sensorEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.sensorEvent.SensorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService{
    @Override
    public void collectEvent(SensorEvent event) {

    }
}
