package com.infoevent.notificationservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    String firstName;
    String lastName;
    String email;
    String ticketType;
    String price;
    String eventName;
    String dateTime;
    String venueName;
    String location;
    byte [] qrCode;
}
