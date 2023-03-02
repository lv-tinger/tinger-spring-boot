package org.tinger.data.core.value;

public enum Preference {
    PRIMARY(1), SECONDARY(2);
    public final int value;

    Preference(int value) {
        this.value = value;
    }
}