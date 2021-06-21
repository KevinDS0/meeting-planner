package com.meetingplanner.domain.enumeration;

/**
 * The Creneau enumeration.
 */
public enum Creneau {
    C1("8h-9h"),
    C2("9h-10h"),
    C3("10h-11h"),
    C4("11h-12h"),
    C5("12h-13h"),
    C6("13h-14h"),
    C7("14h-15h"),
    C8("15h-16h"),
    C9("16h-17h"),
    C10("17h-18h"),
    C11("18h-19h"),
    C12("19h-20h");

    private final String value;

    Creneau(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
