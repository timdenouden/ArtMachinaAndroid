package com.avanade.artmachina.app.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.activities.ArtworkDetailActivity;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtworkFragment extends Fragment {
    public static final int NUM_COLUMBS = 2;

    private ArrayList<Artwork> artworks;
    private RecyclerView galleryList;
    private RecyclerView.Adapter galleryListAdapter;
    private RecyclerView.LayoutManager galleryListLayoutManager;

    public ArtworkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.getInstance(getActivity()).getArtworkList(new DataProvider.ArtworkListCompletion() {
            @Override
            public void complete(List<Artwork> artworkList) {
                artworks = new ArrayList<>(artworkList);
                if(galleryListAdapter != null) {
                    galleryListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(String failureMessage) {
                artworks = new ArrayList<>();
                if(galleryListAdapter != null) {
                    galleryListAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_artwork, container, false);
        galleryList = fragmentView.findViewById(R.id.gallery_list);
        galleryList.setHasFixedSize(true);
        galleryListLayoutManager = new GridLayoutManager(getContext(), NUM_COLUMBS);
        galleryList.setLayoutManager(galleryListLayoutManager);
        galleryListAdapter = new GalleryListAdapter(artworks);
        galleryList.setAdapter(galleryListAdapter);
        int spacingInPixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        galleryList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return fragmentView;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ViewHolder> {

        private ArrayList<Artwork> artworks;

        public GalleryListAdapter(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_artwork, parent, false);
            ViewHolder viewHolder = new GalleryListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Artwork artwork = artworks.get(position);
            viewHolder.gridImage.setImageUrl("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png", DataManager.getInstance(getActivity()).getImageLoader());
            viewHolder.commentCount.setText(artwork.getCommentCount() + "");
            viewHolder.viewCount.setText(artwork.getViewCount() + "");
            viewHolder.rating.setText(String.format("%.1f", artwork.getRating()));
        }

        @Override
        public int getItemCount() {
            return artworks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public NetworkImageView gridImage;
            public TextView viewCount;
            public TextView commentCount;
            public TextView rating;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                gridImage = view.findViewById(R.id.grid_image);
                viewCount = view.findViewById(R.id.view_count);
                commentCount = view.findViewById(R.id.comment_count);
                rating = view.findViewById(R.id.rating);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArtworkDetailActivity.class);
                startActivity(intent);
            }
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            outRect.top = space;
            outRect.bottom = 0;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) % 2 == 1) {
                outRect.left = space / 2;
                outRect.right = space;
            } else {
                outRect.left = space;
                outRect.right = space / 2;
            }
        }
    }
}
