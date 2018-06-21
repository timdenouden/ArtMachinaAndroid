package com.avanade.artmachina.app.fragments;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.activities.ArtworkDetailActivity;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtworkFragment extends Fragment {
    public static final int NUM_COLUMBS = 2;

    private RecyclerView galleryList;
    private GalleryListAdapter galleryListAdapter;
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
                if(galleryListAdapter != null) {
                    galleryListAdapter.setArtworks(new ArrayList<>(artworkList));
                }
            }

            @Override
            public void failure(HttpResponseError responseError) {
                if(galleryListAdapter != null) {
                    galleryListAdapter.setArtworks(null);
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
        galleryListLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMBS, StaggeredGridLayoutManager.VERTICAL);
        galleryList.setLayoutManager(galleryListLayoutManager);
        galleryListAdapter = new GalleryListAdapter();
        galleryList.setAdapter(galleryListAdapter);
        int spacingInPixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        galleryList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return fragmentView;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.ArtworkViewHolder> {

        private ArrayList<Artwork> artworks;

        public GalleryListAdapter() {

        }

        public GalleryListAdapter(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
        }

        public void setArtworks(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ArtworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_artwork, parent, false);
            ArtworkViewHolder artworkViewHolder = new ArtworkViewHolder(view);
            return artworkViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ArtworkViewHolder artworkViewHolder, int position) {
            Artwork artwork = artworks.get(position);
            artworkViewHolder.setArtworkId(artwork.getId());
            artworkViewHolder.gridImage.setImageUrl(artwork.getProcessedImageUrl().toString(), DataManager.getInstance(getActivity()).getImageLoader());
            artworkViewHolder.commentCount.setText(artwork.getCommentCount() + "");
            artworkViewHolder.viewCount.setText(artwork.getViewCount() + "");
            artworkViewHolder.rating.setText(String.format("%.1f", artwork.getRating()));
        }

        @Override
        public int getItemCount() {
            if(artworks == null) {
                return 0;
            }
            return artworks.size();
        }

        public class ArtworkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public NetworkImageView gridImage;
            public TextView viewCount;
            public TextView commentCount;
            public TextView rating;
            private String artworkId;

            public ArtworkViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                gridImage = view.findViewById(R.id.grid_image);
                viewCount = view.findViewById(R.id.view_count);
                commentCount = view.findViewById(R.id.comment_count);
                rating = view.findViewById(R.id.rating);
            }

            public void setArtworkId(String artworkId) {
                this.artworkId = artworkId;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArtworkDetailActivity.class);
                intent.putExtra(Artwork.KEY_NAME_ID, artworkId);
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

            outRect.left = space / 2;
            outRect.right = space / 2;
        }
    }
}
