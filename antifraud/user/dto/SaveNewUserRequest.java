package antifraud.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record SaveNewUserRequest(
        @NotEmpty String name,
        @NotEmpty String username,
        @NotEmpty String password

        ) { }
