package com.infoevent.eventservice.schedules;

import com.infoevent.eventservice.entities.Event;
import com.infoevent.eventservice.entities.EventStatus;
import com.infoevent.eventservice.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Periodically checks and updates the statuses of events based on their scheduled times.
 * <p>
 * This component runs a scheduled task every 5 minutes to update event statuses:
 * - Events starting in more than 30 minutes are marked as ACTIVE.
 * - Events starting within the next 30 minutes or before their end time are marked as IN_PROGRESS.
 * - Events past their end time are marked as FINISHED.
 * Events that are SUSPENDED or CANCELLED are not updated by this task.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventStatusUpdater {

    private final EventRepository eventRepository;


    /**
     * Scheduled task to update event statuses every 5 minutes.
     */
    @Scheduled(fixedRate = 300000) // 300,000 milliseconds = 5 minutes
    public void updateEventStatuses() {
        log.info("Starting the update of event statuses.");
        List<Event> events = eventRepository.findAllByEventStatusNotIn(List.of(EventStatus.SUSPENDED, EventStatus.CANCELLED));

        events.forEach(this::updateEventStatus);

        log.info("Completed updating event statuses.");
    }

    /**
     * Updates the status of a single event based on the current time and event start/end times.
     *
     * @param event The event to update.
     */
    public void updateEventStatus(Event event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventStart = LocalDateTime.of(event.getDate(), event.getStartTime());
        LocalDateTime eventEnd = LocalDateTime.of(event.getDate(), event.getEndTime());

        if (now.isBefore(eventStart.minusMinutes(30))) {
            event.setEventStatus(EventStatus.ACTIVE);
        } else if (now.isAfter(eventStart.minusMinutes(30)) && now.isBefore(eventEnd)) {
            event.setEventStatus(EventStatus.IN_PROGRESS);
        } else if (now.isAfter(eventEnd)) {
            event.setEventStatus(EventStatus.FINISHED);
        }

        eventRepository.save(event);
        log.debug("Updated event status to {} for event: {}", event.getEventStatus(), event.getName());
    }
}
