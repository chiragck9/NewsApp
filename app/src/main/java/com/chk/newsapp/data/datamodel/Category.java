package com.chk.newsapp.data.datamodel;

/**
 * Created by chira on 18-08-2017.
 */

public enum Category {

    BUSINESS("business"),
    GENERAL("general"),
    ENTERTAINMENT("entertainment"),
    GAMING("gaming"),
    MUSIC("music"),
    POLITICS("politics"),
    SCIENCE_AND_NATURE("science-and-nature"),
    SPORTS("sport"),
    TECHNOLOGY("technology"),
    HEALTH("health-and-medical");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static Category fromString(String category) {
        if ("business".equals(category)) {
            return BUSINESS;
        }
        if ("general".equals(category)) {
            return GENERAL;
        }
        if ("entertainment".equals(category)) {
            return ENTERTAINMENT;
        }
        if ("gaming".equals(category)) {
            return GAMING;
        }
        if ("music".equals(category)) {
            return MUSIC;
        }
        if ("politics".equals(category)) {
            return POLITICS;
        }
        if ("science-and-nature".equals(category)) {
            return SCIENCE_AND_NATURE;
        }
        if ("sport".equals(category)) {
            return SPORTS;
        }
        if ("technology".equals(category)) {
            return TECHNOLOGY;
        }
        if ("health-and-medica".equals(category)) {
            return HEALTH;
        }
        return null;
    }
}
