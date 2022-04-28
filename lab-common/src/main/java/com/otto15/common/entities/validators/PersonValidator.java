package com.otto15.common.entities.validators;

import com.otto15.common.entities.Coordinates;
import com.otto15.common.entities.Location;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.enums.Color;
import com.otto15.common.entities.enums.Country;


/**
 * Class for validation of Person class.
 *
 * @author Rakhmatullin R.
 */
public final class PersonValidator {

    private PersonValidator() {

    }

    public static String getValidatedName(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Name field can not be empty.");
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Provide one argument, use \"\" for several words.");
        }
        if (args[0].length() > (Integer.MAX_VALUE / 2)) {
            throw new IllegalArgumentException("String is too long.");
        }
        return args[0];
    }

    public static boolean isNameValid(String name) {
        return name != null;
    }

    public static Coordinates getValidatedCoordinates(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Coordinates field can not be empty");
        }
        if (args.length != 2) {
            throw new IllegalArgumentException("Coordinates field must have 2 arguments.");
        }
        Coordinates coordinates = new Coordinates(CoordinatesValidator.getValidatedX(args[0]), CoordinatesValidator.getValidatedY(args[1]));
        if (isCoordinatesValid(coordinates)) {
            return coordinates;
        } else {
            throw new IllegalArgumentException("Invalid coordinates value");
        }
    }

    public static boolean isCoordinatesValid(Coordinates coordinates) {
        return coordinates != null
                && CoordinatesValidator.isXValid(coordinates.getX())
                && CoordinatesValidator.isYValid(coordinates.getY());
    }

    public static long getValidatedHeight(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Height field can not be empty.");
        } else if (args.length != 1) {
            throw new IllegalArgumentException("Height implies 1 number.");
        }
        try {
            long height = Long.parseLong(args[0]);
            if (isHeightValid(height)) {
                return height;
            } else {
                throw new IllegalArgumentException("Invalid height value");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input of height.");
        }
    }

    public static boolean isHeightValid(long height) {
        return height > 0;
    }

    public static Color getValidatedColor(String[] args) {
        if (args.length == 0) {
            return null;
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("Color implies 1 value.");
        }
        return ColorValidator.getValidatedColor(args[0].toUpperCase());
    }

    public static Country getValidatedCountry(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Country can not be empty");
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("Country implies 1 value.");
        }
        return CountryValidator.getValidatedColor(args[0]);
    }

    public static boolean isCountryValid(Country country) {
        return country != null;
    }

    public static Location getValidatedLocation(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Location field can not be empty.");
        }
        if (args.length != Location.class.getDeclaredFields().length) {
            throw new IllegalArgumentException("Location implies 3 values.");
        }
        return new Location(LocationValidator.getValidatedX(args[0]),
                LocationValidator.getValidatedY(args[1]),
                LocationValidator.getValidatedZ(args[2]));
    }

    public static boolean isLocationValid(Location location) {
        return location != null;
    }

    public static boolean isPersonValid(Person person) {
        return isNameValid(person.getName())
                && isHeightValid(person.getHeight())
                && isCountryValid(person.getNationality())
                && isCoordinatesValid(person.getCoordinates())
                && isLocationValid(person.getLocation());
    }
}
