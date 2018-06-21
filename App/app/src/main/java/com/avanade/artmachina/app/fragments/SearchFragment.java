package com.avanade.artmachina.app.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private RecyclerView searchList;
    private SearchListAdapter searchListAdapter;
    private RecyclerView.LayoutManager searchListLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.getInstance(getContext()).getArtworkList(new DataProvider.ArtworkListCompletion() {
            @Override
            public void complete(List<Artwork> artworkList) {
                if(searchListAdapter != null) {
                    searchListAdapter.setArtworks(new ArrayList<Artwork>(artworkList));
                }
            }

            @Override
            public void failure(HttpResponseError error) {
                if(searchListAdapter != null) {
                    searchListAdapter.setArtworks(null);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        searchList = fragmentView.findViewById(R.id.search_list);
        searchList.setHasFixedSize(true);
        searchListLayoutManager = new LinearLayoutManager(getContext());
        searchList.setLayoutManager(searchListLayoutManager);
        searchListAdapter = new SearchListAdapter();
        searchList.setAdapter(searchListAdapter);
        //int spacingInPixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        //galleryList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return fragmentView;
    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
        private ArrayList<Artwork> artworks;

        public SearchListAdapter() {

        }

        public SearchListAdapter(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
        }

        public void setArtworks(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.search_item_artwork, parent, false);
            ViewHolder viewHolder = new SearchListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Artwork artwork = artworks.get(position);
            viewHolder.searchImage.setImageUrl(artwork.getProcessedImageUrl().toString(), DataManager.getInstance(getActivity()).getImageLoader());
            viewHolder.artworkTitle.setText(artwork.getTitle());
        }

        @Override
        public int getItemCount() {
            if(artworks == null) {
                return 0;
            }
            return artworks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public NetworkImageView searchImage;
            public TextView artworkTitle;

            public ViewHolder(View view) {
                super(view);
                searchImage = view.findViewById(R.id.list_image);
                artworkTitle = view.findViewById(R.id.list_title);
            }
        }
    }

}
