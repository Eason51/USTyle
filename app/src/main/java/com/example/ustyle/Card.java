package com.example.ustyle;

public class Card {

    // Member variables representing the title and image about the sport.
    private String title;
    private String info;
    private String description;
    private String imageResource;

    /**
     * Constructor for the card data model.
     *
     * @param title The name of the facility.
     * @param imageResource Image resource of the facility.
     */
    Card(String title, String info, String description, String imageResource) {
        this.title = title;
        this.info = info;
        this.description = description;
        this.imageResource = imageResource;
    }

    /**
     * Gets the title of the facility.
     *
     * @return The title of the facility.
     */
    String getTitle() {
        return title;
    }

    String getInfo() {
        return info;
    }

    String getDescription() {
        return description;
    }

    public String getImageResource() {
        return imageResource;
    }

}
