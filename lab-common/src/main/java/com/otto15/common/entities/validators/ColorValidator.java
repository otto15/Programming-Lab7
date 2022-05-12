package com.otto15.common.entities.validators;

import com.otto15.common.entities.enums.Color;

/**
 * Class for validation Color enum.
 *
 * @author Rakhmatullin R.
 */
public final class ColorValidator {

    private ColorValidator() {

    }

    public static Color getValidatedColor(String colorArg) {
        try {
            return Color.valueOf(colorArg);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid color.");
        }
    }
}
