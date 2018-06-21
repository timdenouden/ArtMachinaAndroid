package com.avanade.artmachina.app.models;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Artwork {
    public static final String KEY_NAME_ID = "artworkId";

    private String id;

    private String title;

    private String description;

    private String updatedAt;

    private String author;

    private int viewCount;

    private double rating;

    private int ratingCount;

    private int commentCount;

    private URL sourceImageUrl;

    private int sourceImageWidth;

    private int sourceImageHeight;

    @SerializedName("afterImageUrl")
    private URL processedImageUrl;

    @SerializedName("width")
    private int processedImageWidth;

    @SerializedName("height")
    private int processedImageHeight;

    private URL referenceImageUrl;

    private int referenceImageWidth;

    private int referenceImageHeight;

    private int myRating;

    private boolean isBookmarked;

    public Artwork(String id, String title, String description, String updatedAt, String author, int viewCount, double rating, int ratingCount, int commentCount, URL sourceImageUrl, int sourceImageWidth, int sourceImageHeight, URL processedImageUrl, int processedImageWidth, int processedImageHeight, URL referenceImageUrl, int referenceImageWidth, int referenceImageHeight, int myRating, boolean isBookmarked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
        this.author = author;
        this.viewCount = viewCount;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.commentCount = commentCount;
        this.sourceImageUrl = sourceImageUrl;
        this.sourceImageWidth = sourceImageWidth;
        this.sourceImageHeight = sourceImageHeight;
        this.processedImageUrl = processedImageUrl;
        this.processedImageWidth = processedImageWidth;
        this.processedImageHeight = processedImageHeight;
        this.referenceImageUrl = referenceImageUrl;
        this.referenceImageWidth = referenceImageWidth;
        this.referenceImageHeight = referenceImageHeight;
        this.myRating = myRating;
        this.isBookmarked = isBookmarked;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public int getViewCount() {
        return viewCount;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public URL getSourceImageUrl() {
        return sourceImageUrl;
    }

    public int getSourceImageWidth() {
        return sourceImageWidth;
    }

    public int getSourceImageHeight() {
        return sourceImageHeight;
    }

    public URL getProcessedImageUrl() {
        return processedImageUrl;
    }

    public int getProcessedImageWidth() {
        return processedImageWidth;
    }

    public int getProcessedImageHeight() {
        return processedImageHeight;
    }

    public URL getReferenceImageUrl() {
        return referenceImageUrl;
    }

    public int getReferenceImageWidth() {
        return referenceImageWidth;
    }

    public int getReferenceImageHeight() {
        return referenceImageHeight;
    }

    public int getMyRating() {
        return myRating;
    }

    public void setMyRating(int myRating) {
        this.myRating = myRating;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}
