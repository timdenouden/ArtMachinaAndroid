package com.avanade.artmachina.app.models;

import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

public class StaticDataProvider implements DataProvider {

    public StaticDataProvider() {

    }

    @Override
    public void login(User userCredential, AuthCompletion completion) {

    }

    @Override
    public void register(User newUser, AuthCompletion completion) {

    }

    @Override
    public void getProfile(String token, ProfileCompletion completion) {

    }

    @Override
    public void updateProfile(String token, User profile, ProfileCompletion completion) {

    }

    @Override
    public void getArtworkList(String token, ArtworkListCompletion completion) {
        ArrayList<Artwork> artworks = new ArrayList<>();
        try {
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));
            artworks.add(new Artwork(
                    5,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false));

        } catch (Exception e) {
            // do nothing for now
        }
        completion.complete(artworks);
    }

    @Override
    public void getCommentList(String token, CommentListCompletion completion) {

    }

    @Override
    public void postComment(String token, String content) {

    }

    @Override
    public void deleteComment(String token, int commentId) {

    }

    @Override
    public void reportComment(String token, int commentId) {

    }

    @Override
    public void getArtwork(String token, int artworkId, ArtworkCompletion completion) {
        Artwork artwork = null;
        try {
            artwork = new Artwork(
                    artworkId,
                    "Self-Portrait (1889)",
                    "Possibly van Gogh’s final self-portrait, it shows van Gogh after he mutilated his ear and voluntarily entered an asylum. Van Gogh is instantly recognizable by his reddish hair and beard, his gaunt features, and intense gaze. Van Gogh painted some 36 self-portraits in the space of only ten years. In the painting the attention is focused on the face. His features are hard and emaciated, his green-rimmed eyes seem intransigent and anxious. The dominant colour, a mix of absinth green and pale turquoise finds a counterpoint in its complementary colour, the fiery orange of the beard and hair. The model's immobility contrasts with the undulating hair and beard, echoed and amplified in the hallucinatory arabesques of the background.",
                    "2018-06-05T11:25:24.551268-07:00",
                    "Vincent Van Gogh",
                    1,
                    3.33,
                    5,
                    2,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/src_Self-Portrait_1889.jpg"),
                    3024,
                    4032,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png"),
                    600,
                    800,
                    new URL("https://avanadeprojectstorage.blob.core.windows.net/art-machina/ref_Self-Portrait_1889.jpg"),
                    3142,
                    3820,
                    3,
                    false);
        } catch (Exception e) {

        }

        Log.d(this.getClass().getSimpleName(), artwork.getAuthor());
        completion.complete(artwork);
    }

    @Override
    public void updateBookmark(String token, int artworkId, boolean newBookmarkState) {

    }

    @Override
    public void updateRating(String token, int artworkId, int newRating) {

    }

    @Override
    public void getBookmarkList(String token, ArtworkListCompletion completion) {

    }

    @Override
    public void getPasswordResetUrl(UrlCompletion completion) {

    }
}
