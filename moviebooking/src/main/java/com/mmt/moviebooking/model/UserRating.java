package com.mmt.moviebooking.model;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public class UserRating {

    private int rating = 0; // rating to be considered as out of 100.
    private String comment = "";

    public int getRating() {
        return rating;
    }

    public UserRating setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public UserRating setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public UserRating(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public UserRating(int rating) {
        this.rating = rating;
    }
}
