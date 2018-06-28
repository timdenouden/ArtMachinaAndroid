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

    interface EmptyCompletion extends FailureListener {
        void complete();
    }

    void login(User userCredential, AuthCompletion completion);
    void register(User newUser, AuthCompletion completion);
    void getProfile(String token, ProfileCompletion completion);
    void updateProfile(String token, User profile, ProfileCompletion completion);
    void getArtworkList(String token, ArtworkListCompletion completion);
    void getCommentList(String token, String artworkId, CommentListCompletion completion);
    void postComment(String token, String artworkId, String content, EmptyCompletion completion);
    void deleteComment(String token, String commentId, EmptyCompletion completion);
    void reportComment(String token, String commentId, EmptyCompletion completion);
    void getArtwork(String token, String artworkId, ArtworkCompletion completion);
    void updateBookmark(String token, String artworkId, boolean newBookmarkState, EmptyCompletion completion);
    void updateRating(String token, String artworkId, int newRating, EmptyCompletion completion);
    void getBookmarkList(String token, ArtworkListCompletion completion);
    void getPasswordResetUrl(UrlCompletion completion);

}
