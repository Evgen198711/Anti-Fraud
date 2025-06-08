package antifraud.transaction.dto;

public record Limits(String cardNumber, int allowedLimit, int manualLimit) {
}
