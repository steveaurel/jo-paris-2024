package com.infoevent.authservice.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private String password;

    private String accessToken;

    private String refreshToken;
}
