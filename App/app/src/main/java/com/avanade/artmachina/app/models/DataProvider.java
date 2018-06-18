package com.avanade.artmachina.app.models;
import java.util.List;

public interface DataProvider {

    interface AuthCompletion {
        void complete(String token);
    }

    interface ProfileCompletion {
        void complete(User profile);
    }

    interface ArtworkListCompletion {
        void complete(List<Artwork> artworkList);
        void failure(String failureMessage);
    }

    interface CommentListCompletion {
        void complete(List<Comment> commentList);
    }

    interface ArtworkCompletion {
        void complete(Artwork artwork);
        void failure(String failureMessage);
    }

    interface UrlCompletion {
        void complete(String url);
    }

    void login(User userCredential, AuthCompletion completion);
    void register(User newUser, AuthCompletion completion);
    void getProfile(String token, ProfileCompletion completion);
    void updateProfile(String token, User profile, ProfileCompletion completion);
    void getArtworkList(String token, ArtworkListCompletion completion);
    void getCommentList(String token, CommentListCompletion completion);
    void postComment(String token, String content);
    void deleteComment(String token, int commentId);
    void reportComment(String token, int commentId);
    void getArtwork(String token, int artworkId, ArtworkCompletion completion);
    void updateBookmark(String token, int artworkId, boolean newBookmarkState);
    void updateRating(String token, int artworkId, int newRating);
    void getBookmarkList(String token, ArtworkListCompletion completion);
    void getPasswordResetUrl(UrlCompletion completion);


}
