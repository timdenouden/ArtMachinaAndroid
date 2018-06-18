package com.avanade.artmachina.app.models;

import android.content.Context;
import android.graphics.Bitmap;
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
        dataProvider = new StaticDataProvider();
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

    public void getArtworkList(DataProvider.ArtworkListCompletion completion) {
        dataProvider.getArtworkList("token", completion);
    }

    public void getArtwork(int id, DataProvider.ArtworkCompletion completion) {

    }
}
