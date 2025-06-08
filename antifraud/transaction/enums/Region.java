package antifraud.transaction.enums;

import antifraud.transaction.exceptions.WrongFormatException;

public enum Region {EAP("EAP"), ECA("ECA"), HIC("HIC"), LAC("LAC"), MENA("MENA"), SA("SA"), SSA("SSA");

    private final String region;

    private Region(String region) {
        this.region = region;
    }

    public static Region of(String region) {
        for(Region r : values()) {
            if (r.region.equals(region)) {
                return r;
            }
        }
        throw new WrongFormatException("Wrong region: " + region);
    }
}
