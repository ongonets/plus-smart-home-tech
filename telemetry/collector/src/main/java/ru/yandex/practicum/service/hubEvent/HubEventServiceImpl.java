package ru.yandex.practicum.service.hubEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.hubEvent.HubEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubEventServiceImpl  implements HubEventService {
    @Override
    public void collectEvent(HubEvent event) {

    }
}
