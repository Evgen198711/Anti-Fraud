package antifraud.transaction.enums;

public enum Info {AMOUNT("amount"), CARD_NUMBER("card-number"), IP("ip"), IP_CORRELATION("ip-correlation"), REGION_CORRELATION("region-correlation"), NONE("none");
    private final String reason;

    private Info(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
