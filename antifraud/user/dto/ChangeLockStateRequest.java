package antifraud.user.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeLockStateRequest(
        @NotNull String username,
        @NotNull String operation
) {
}
