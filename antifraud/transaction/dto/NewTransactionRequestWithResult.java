package antifraud.transaction.dto;

import java.time.LocalDateTime;

public record NewTransactionRequestWithResult(long amount, String ip, String number, String region, LocalDateTime date, String result) {
}
