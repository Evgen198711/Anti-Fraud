package antifraud.transaction.enums;

public enum Result {ALLOWED("ALLOWED"), MANUAL_PROCESSING("MANUAL_PROCESSING"), PROHIBITED("PROHIBITED");

    private final String result;

    private Result(String result) {
        this.result = result;
    }

    public static Result of(String result) {
        for(Result r : values()) {
            if (r.result.equals(result)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unknown result: " + result);
    }

    public String getResult() {
        return result;
    }
}
