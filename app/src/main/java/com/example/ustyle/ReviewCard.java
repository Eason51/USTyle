package com.example.ustyle;

public class ReviewCard {

    // Member variables representing the title and image about the sport.
    private String username;
    private String content;

    /**
     * Constructor for the ReviewCard data model.
     *
     * @param username
     * @param content
     */
    ReviewCard(String username, String content) {
        this.username = username;
        this.content = content;
    }

    String getUsername() {
        return username;
    }

    String getContent() {
        return content;
    }
}