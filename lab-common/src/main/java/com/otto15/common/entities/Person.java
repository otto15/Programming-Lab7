package com.otto15.common.entities;

import com.otto15.common.entities.enums.Color;
import com.otto15.common.entities.enums.Country;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Represent person with its properties.
 */
public class Person implements Comparable<Person>, Serializable {

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private ZonedDateTime creationDate = ZonedDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotBlank
    @SetByUser(invitation = "Enter name(e.g \"Hasbulla\")")
    private String name; //Поле не может быть null, Строка не может быть пустой

    private String author;

    @Valid
    @NotNull
    @SetByUser(invitation = "Enter coordinates(enter x and y separated by space,e.g \"15.5 12\")")
    private Coordinates coordinates; //Поле не может быть null //

    @Positive
    @SetByUser(invitation = "Enter height(enter integer number)")
    private long height; //Значение поля должно быть больше 0 //

    @SetByUser(invitation = "Enter eye color(choose color from the list below, field can be empty)")
    private Color eyeColor; //Поле может быть null

    @SetByUser(invitation = "Enter hair color(choose color from the list below, field can be empty)")
    private Color hairColor; //Поле может быть null

    @NotNull
    @SetByUser(invitation = "Enter nationality(choose country from the list below, field can not be empty)")
    private Country nationality; //Поле не может быть null

    @NotNull
    @SetByUser(invitation = "Enter location(enter x, y, z separated by space,e.g \"15.5 99.99 12\")")
    private Location location; //Поле не может быть null

    public Person() {

    }

    public Person(String name, Coordinates coordinates, long height, Color eyeColor, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates field can not be empty.");
        }
        this.coordinates = coordinates;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height field must be greater than zero.");
        }
        this.height = height;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location field can not be empty.");
        }
        this.location = location;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Nationality field can not be empty.");
        }
        this.nationality = nationality;
    }

    @Override
    public int compareTo(Person anotherPerson) {
        return Comparator.comparing(Person::getName).thenComparingLong(Person::getHeight).compare(anotherPerson, this);
    }

    @Override
    public String toString() {
        return "id: " + id + ", author: " + author + ", name: " + name + ", coordinates: " + coordinates + ", height: " + height + ", creationDate: " + creationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm")) + ", eyeColor: " + eyeColor + ", hairColor: " + hairColor + ", nationality: " + nationality + ", location: " + location;
    }

    public String currentValues() {
        return name + " \"" + coordinates + "\" " + height + " " + eyeColor + " " + hairColor + " " + nationality + " \"" + location + "\"";
    }
}
