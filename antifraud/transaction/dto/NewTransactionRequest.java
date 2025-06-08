package antifraud.transaction.dto;

import jakarta.validation.constraints.NotEmpty;

public record NewTransactionRequest(long amount, @NotEmpty String ip, @NotEmpty String number, @NotEmpty String region, String date) {
}
