package com.otto15.common.entities.enums;

import java.io.Serializable;

public enum Color implements Serializable {
    GREEN,
    YELLOW,
    ORANGE,
    WHITE,
    BROWN,
    RED,
    BLACK,
    BLUE;

    public static String getAvailableColorNames() {
        StringBuilder colors = new StringBuilder();
        for (Color color: Color.values()) {
            colors.append(color.name());
            colors.append(" ");
        }
        return colors.toString();
    }
}
