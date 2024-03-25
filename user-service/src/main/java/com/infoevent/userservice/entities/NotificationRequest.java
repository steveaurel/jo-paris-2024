package com.infoevent.userservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    String firstName;
    String lastName;
    String ticketType;
}
