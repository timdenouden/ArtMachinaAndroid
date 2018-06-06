package com.avanade.artmachina.app.models;

public class Artwork {
    private String id;
    private String title;
    private String description;
    private float height;
    private float width;
    private String referenceImageUrl;
    private String sourceImageUrl;
    private String afterImageUrl;
    private String author;
    private float rating;
    private int ratingCount;
    private int commentCount;
    private int viewCount;
    private boolean isBookmarked;
    private int myRating;

    public Artwork() {

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public String getReferenceImageUrl() {
        return referenceImageUrl;
    }

    public String getSourceImageUrl() {
        return sourceImageUrl;
    }

    public String getAfterImageUrl() {
        return afterImageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public float getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public int getMyRating() {
        return myRating;
    }
}
