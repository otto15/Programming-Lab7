package com.otto15.common.entities.enums;

import java.io.Serializable;

public enum Country implements Serializable {
    SPAIN("Spain"),
    CHINA("China"),
    ITALY("Italy");

    private final String name;

    Country(String name) {
        this.name = name;
    }

    public String getRepresentation() {
        return name;
    }

    public static String getAvailableCountryNames() {
        StringBuilder countries = new StringBuilder();
        for (Country country: Country.values()) {
            countries.append(country.name());
            countries.append(" ");
        }
        return countries.toString();
    }
}
