package com.otto15.common.entities.validators;

import com.otto15.common.entities.Coordinates;

/**
 * Class for validation of Coordinates.
 *
 * @author Rakhmatullin R.
 */
public final class CoordinatesValidator {

    private CoordinatesValidator() {

    }

    public static double getValidatedX(String xArg) {
        try {
            double x = Double.parseDouble(xArg);
            if (Double.isInfinite(x)) {
                throw new IllegalArgumentException("Incorrect value for x argument");
            }
            return x;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates' argument value.");
        }
    }

    public static boolean isXValid(double x) {
        return x <= Coordinates.X_MAX_VALUE;
    }

    public static Double getValidatedY(String yArg) {
        try {
            double y = Double.parseDouble(yArg);
            if (Double.isInfinite(y)) {
                throw new IllegalArgumentException("Incorrect value for x argument");
            }
            return y;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates' argument value.");
        }
    }

    public static boolean isYValid(Double y) {
        return y != null && y > Coordinates.Y_MIN_VALUE;
    }

}
