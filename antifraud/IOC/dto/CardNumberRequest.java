package antifraud.IOC.dto;

import jakarta.validation.constraints.NotEmpty;

public record CardNumberRequest(@NotEmpty String number) {
}
