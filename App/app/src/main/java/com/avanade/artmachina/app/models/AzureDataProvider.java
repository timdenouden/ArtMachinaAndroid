package com.avanade.artmachina.app.models;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AzureDataProvider implements DataProvider {

    private RequestQueue requestQueue;

    private Gson gson;

    private static String BASE_URL = "https://avanademobileapp.azurewebsites.net/api/";

    public AzureDataProvider(RequestQueue requestQueue){
        this.requestQueue = requestQueue;
        this.gson = new Gson();
    }


    @Override
    public void login(User userCredential, final AuthCompletion completion) {
        try {
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "auth",
                    new JSONObject(gson.toJson(userCredential)), getAuthListener(completion), getErrorListener(completion));

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void register(User newUser, AuthCompletion completion) {

        try {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_URL + "auth",
                    new JSONObject(gson.toJson(newUser)), getAuthListener(completion), getErrorListener(completion));
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getProfile(final String token, final ProfileCompletion completion) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL + "profile", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        completion.complete(gson.fromJson(response.toString(),User.class));
                    }
                }, getErrorListener(completion))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getTokenHeader(token);
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void updateProfile(final String token, User profile, final ProfileCompletion completion){
        try{
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "profile",
                    new JSONObject(gson.toJson(profile)),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            completion.complete(gson.fromJson(response.toString(),User.class));
                        }
                    }, getErrorListener(completion))
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getTokenHeader(token);
                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getArtworkList(String token, final ArtworkListCompletion completion) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, BASE_URL + "artworks", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Artwork> artworkList = new ArrayList<>();
                        for (int i=0;i <response.length();i++){
                            artworkList.add(gson.fromJson(response.optJSONObject(i).toString(),Artwork.class));
                        }
                        completion.complete(artworkList);
                    }
                },getErrorListener(completion));
        requestQueue.add(request);
    }

    @Override
    public void getCommentList(String token, CommentListCompletion completion) {

    }

    @Override
    public void postComment(String token, String content) {

    }

    @Override
    public void deleteComment(String token, String commentId) {

    }

    @Override
    public void reportComment(String token, String commentId) {

    }

    @Override
    public void getArtwork(String token, String artworkId, ArtworkCompletion completion) {
        Artwork artwork = null;
        try {
            artwork = new Artwork(
                    "id",
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
        completion.complete(artwork);
    }

    @Override
    public void updateBookmark(String token, String artworkId, boolean newBookmarkState) {

    }

    @Override
    public void updateRating(String token, String artworkId, int newRating) {

    }


    @Override
    public void getBookmarkList(String token, ArtworkListCompletion completion) {

    }

    @Override
    public void getPasswordResetUrl(UrlCompletion completion) {

    }

    private Response.ErrorListener getErrorListener(final DataProvider.FailureListener failureListener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    failureListener.failure(new HttpResponseError(error.networkResponse.statusCode,
                            new String(error.networkResponse.data)));
                }
            }
        };
    }

    private Response.Listener<JSONObject> getAuthListener(final DataProvider.AuthCompletion authCompletion) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                boolean success = response.optBoolean("success");
                String payload = response.optString("payload");
                if (success) {
                    authCompletion.complete(payload);
                } else {
                    authCompletion.failure(new HttpResponseError(200, payload));
                }
            }
        };
    }

    private Map<String,String> getTokenHeader(String token) {
        Map<String,String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization","Bearer "+token);
        return header;
    }

}
