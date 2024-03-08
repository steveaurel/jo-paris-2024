package com.infoevent.eventservice.entities;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventDuration {
    private int hours;
    private int minutes;
    public EventDuration() {
    }

    public EventDuration(int hours, int minutes) {
        setHours(hours);
        setMinutes(minutes);
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if (hours < 0 || hours > 24) {
            throw new IllegalArgumentException("Hours must be between 0 and 24.");
        }
        if (hours == 24 && this.minutes > 0) {
            throw new IllegalArgumentException("Duration cannot exceed 24 hours.");
        }
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        if (minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59.");
        }
        if (this.hours == 24 && minutes > 0) {
            throw new IllegalArgumentException("Duration cannot exceed 24 hours.");
        }
        this.minutes = minutes;
    }

    public void validateTotalDuration() {
        if (this.hours == 24 && this.minutes > 0) {
            throw new IllegalArgumentException("Duration cannot exceed 24 hours.");
        }
    }
}