package com.avanade.artmachina.app.models;
import java.util.List;

public interface DataProvider {

    interface FailureListener {
        void failure(HttpResponseError error);
    }

    interface AuthCompletion extends FailureListener {
        void complete(String token);
    }

    interface ProfileCompletion extends FailureListener {
        void complete(User profile);
    }

    interface ArtworkListCompletion extends FailureListener {
        void complete(List<Artwork> artworkList);
    }

    interface CommentListCompletion extends FailureListener {
        void complete(List<Comment> commentList);
    }

    interface ArtworkCompletion extends FailureListener {
        void complete(Artwork artwork);
    }

    interface UrlCompletion extends FailureListener {
        void complete(String url);
    }

    void login(User userCredential, AuthCompletion completion);
    void register(User newUser, AuthCompletion completion);
    void getProfile(String token, ProfileCompletion completion);
    void updateProfile(String token, User profile, ProfileCompletion completion);
    void getArtworkList(String token, ArtworkListCompletion completion);
    void getCommentList(String token, CommentListCompletion completion);
    void postComment(String token, String content);
    void deleteComment(String token, String commentId);
    void reportComment(String token, String commentId);
    void getArtwork(String token, String artworkId, ArtworkCompletion completion);
    void updateBookmark(String token, String artworkId, boolean newBookmarkState);
    void updateRating(String token, String artworkId, int newRating);
    void getBookmarkList(String token, ArtworkListCompletion completion);
    void getPasswordResetUrl(UrlCompletion completion);

}
