package com.avanade.artmachina.app.models;

import com.google.gson.annotations.SerializedName;

public class Comment {

    private int id;

    @SerializedName("reviewer_name")
    private String reviewerName;

    private String content;

    private boolean editable;

    public int getId() {
        return id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getContent() {
        return content;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
