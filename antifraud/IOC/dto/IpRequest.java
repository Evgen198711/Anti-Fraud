package antifraud.IOC.dto;

import jakarta.validation.constraints.NotEmpty;

public record IpRequest(@NotEmpty String ip) {
}
