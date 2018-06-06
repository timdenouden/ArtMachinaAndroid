package com.avanade.artmachina.app.models;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Artwork {

    private int id;

    private String title;

    private String description;

    @SerializedName("updated_at")
    private String updatedAt;

    private String author;

    @SerializedName("view_count")
    private int viewCount;

    private double rating;

    @SerializedName("rating_count")
    private int ratingCount;

    @SerializedName("comment_count")
    private int commentCount;

    @SerializedName("source_image_url")
    private URL sourceImageUrl;

    @SerializedName("source_image_width")
    private int sourceImageWidth;

    @SerializedName("source_image_height")
    private int sourceImageHeight;

    @SerializedName("processed_image_url")
    private URL processedImageUrl;

    @SerializedName("processed_image_width")
    private int processedImageWidth;

    @SerializedName("processed_image_height")
    private int processedImageHeight;

    @SerializedName("reference_image_url")
    private URL referenceImageUrl;

    @SerializedName("reference_image_width")
    private int referenceImageWidth;

    @SerializedName("reference_image_height")
    private int referenceImageHeight;

    @SerializedName("my_rating")
    private int myRating;

    @SerializedName("is_bookmarked")
    private boolean isBookmarked;

    public int getId() {
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
