package com.gerardnico.html.util;

import java.util.Objects;

public class KeyIndependent {

    private final String value;

    public KeyIndependent(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KeyIndependent that = (KeyIndependent) o;
        return Objects.equals(value.toLowerCase(), that.value.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
