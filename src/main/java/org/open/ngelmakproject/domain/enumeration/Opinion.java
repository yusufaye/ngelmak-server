package org.open.ngelmakproject.domain.enumeration;

/**
 * The Opinion enumeration.
 */
public enum Opinion {
    DEFAULT,
    OPPOSED,
    SUPPORT,
    NEUTRAL,
    STRENGTHENED("REINFORCED");

    private String value;

    Opinion() {}

    Opinion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
