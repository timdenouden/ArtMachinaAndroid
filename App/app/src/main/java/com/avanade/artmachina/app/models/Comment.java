package com.avanade.artmachina.app.models;

import com.google.gson.annotations.SerializedName;

public class Comment {

    private String id;

    @SerializedName("reviewer")
    private String reviewerName;

    private String content;

    private String artworkId;

    private boolean editable;

    public String getId() {
        return id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getContent() {
        return content;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setArtworkId(String artworkId) {
        this.artworkId = artworkId;
    }
}
