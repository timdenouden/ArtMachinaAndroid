package com.avanade.artmachina.app.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final int IMAGE_CACHE_MAX_SIZE = 10;

    private static DataManager instance;

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private DataProvider dataProvider;
    private String token = "";
    private User user = null;

    private DataManager(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<>(IMAGE_CACHE_MAX_SIZE);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        // Note: this is just for testing
        // dataProvider = new StaticDataProvider();
        dataProvider = new AzureDataProvider(requestQueue);


    }

    public static DataManager getInstance(Context context) {
        if(instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        if(this.user != null) {
            return this.user;
        }
        return new User();
    }

    public boolean isLoggedIn() {
        return this.token.length() > 0;
    }

    public void logOut() {
        this.token = "";
    }

    public void getArtworkList(DataProvider.ArtworkListCompletion completion) {
        dataProvider.getArtworkList(this.token, completion);
    }

    public void getArtwork(String id, DataProvider.ArtworkCompletion completion) {
        dataProvider.getArtwork(this.token, id, completion);
    }

    public void getComments(String artworkId, DataProvider.CommentListCompletion completion) {
        dataProvider.getCommentList(this.token, artworkId, completion);
    }

    public void addComment(String artworkId, String content, DataProvider.EmptyCompletion completion) {
        dataProvider.postComment(this.token, artworkId, content, completion);
    }

    public void login(User userCredential, DataProvider.AuthCompletion completion) {
        dataProvider.login(userCredential, completion);
    }

    public void getResetUrl(DataProvider.UrlCompletion completion) {
        dataProvider.getPasswordResetUrl(completion);
    }

    public void getProfile(DataProvider.ProfileCompletion completion) {
        if(token.length() > 0) {
            dataProvider.getProfile(this.token, completion);
        }
        else {
            completion.failure(new HttpResponseError(400, "Bad Request"));
        }
    }

    public void updateProfile(User profile, DataProvider.ProfileCompletion completion) {
        dataProvider.updateProfile(this.token, profile, completion);
    }
}
