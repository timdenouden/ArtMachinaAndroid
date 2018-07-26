package com.avanade.artmachina.app.models;

import android.app.VoiceInteractor;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AzureDataProvider implements DataProvider {


    private class JsonObjectRequestWithAuthHeader extends JsonObjectRequest {

        private String token;

        public JsonObjectRequestWithAuthHeader(String token, int method, String url, JSONObject jsonRequest,
                                               Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.token = token;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Authorization","Bearer "+token);
            return header;
        }
    }

    private class JsonArrayRequestWithAuthHeader extends JsonArrayRequest {

        private String token;

        public JsonArrayRequestWithAuthHeader(String token, int method, String url, JSONArray jsonRequest,
                                              Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
            this.token = token;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String,String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("Authorization","Bearer "+token);
            return header;
        }
    }

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
        JsonObjectRequest request = new JsonObjectRequestWithAuthHeader(token,Request.Method.GET,
                BASE_URL + "profile", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        completion.complete(gson.fromJson(response.toString(),User.class));
                    }
                }, getErrorListener(completion));
        requestQueue.add(request);
    }

    @Override
    public void updateProfile(final String token, User profile, final ProfileCompletion completion){
        try{
            JSONObject body = new JSONObject(gson.toJson(profile));
            JsonObjectRequest req = new JsonObjectRequestWithAuthHeader(
                token,
                Request.Method.POST,
            BASE_URL + "profile",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        completion.complete(gson.fromJson(response.toString(),User.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        completion.failure(new HttpResponseError(500, "Server Error"));
                    }
                });
            requestQueue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getArtworkList(String token, final ArtworkListCompletion completion) {

        JsonArrayRequest request = new JsonArrayRequestWithAuthHeader(token,Request.Method.GET, BASE_URL + "artworks", null,
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
    public void getCommentList(String token, String artworkId, final CommentListCompletion completion) {

        JsonArrayRequest request = new JsonArrayRequestWithAuthHeader(token, Request.Method.GET, BASE_URL + "comments?artworkId="+artworkId,
                null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Comment> commentList = new ArrayList<>();
                        for (int i=0;i<response.length();i++){
                            commentList.add(gson.fromJson(response.optJSONObject(i).toString(),Comment.class));
                        }
                        completion.complete(commentList);
                    }
                },getErrorListener(completion));
        requestQueue.add(request);
    }

    @Override
    public void postComment(String token, String artworkId, String content, final EmptyCompletion completion) {

        Comment newComment = new Comment();
        newComment.setArtworkId(artworkId);
        newComment.setContent(content);
        try {
            JsonObjectRequest request = new JsonObjectRequestWithAuthHeader(token, Request.Method.POST, BASE_URL + "comments",
                    new JSONObject(gson.toJson(newComment)), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    completion.complete();
                }
            },getErrorListener(completion));
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void deleteComment(String token, String commentId, final EmptyCompletion completion) {

        JsonObjectRequest request = new JsonObjectRequestWithAuthHeader(token, Request.Method.DELETE, BASE_URL + "comments?commentId=" + commentId,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                completion.complete();
            }
        },getErrorListener(completion));
        requestQueue.add(request);
    }

    @Override
    public void reportComment(String token, String commentId, EmptyCompletion completion) {
    }


    @Override
    public void getArtwork(String token, String artworkId, final ArtworkCompletion completion) {

        JsonObjectRequest request = new JsonObjectRequestWithAuthHeader(token, Request.Method.GET, BASE_URL + "artworks?artworkId=" + artworkId,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                completion.complete(gson.fromJson(response.toString(),Artwork.class));
            }
        },getErrorListener(completion));
        requestQueue.add(request);
    }

    @Override
    public void updateBookmark(String token, String artworkId, boolean newBookmarkState, EmptyCompletion completion) {

    }

    @Override
    public void updateRating(String token, String artworkId, int newRating, EmptyCompletion completion) {

    }


    @Override
    public void getBookmarkList(String token, ArtworkListCompletion completion) {

    }

    @Override
    public void getPasswordResetUrl(UrlCompletion completion) {
        completion.failure(new HttpResponseError(501, "Unimplemented"));
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



}
