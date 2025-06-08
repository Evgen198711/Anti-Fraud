package antifraud.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record ChangeRoleRequest(
        @NotEmpty String username,
        @NotEmpty String role
) {
}
