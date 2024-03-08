package com.infoevent.eventservice.utils;

import com.infoevent.eventservice.entities.EventDuration;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventDurationConverter implements AttributeConverter<EventDuration, String> {

    @Override
    public String convertToDatabaseColumn(EventDuration attribute) {
        if (attribute == null) return null;
        return String.format("%02d:%02d", attribute.getHours(), attribute.getMinutes());
    }

    @Override
    public EventDuration convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        String[] parts = dbData.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return new EventDuration(hours, minutes);
    }
}
