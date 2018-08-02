package com.avanade.artmachina.app.fragments;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
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
public class SearchFragment extends Fragment {
    private RecyclerView searchList;
    private EditText editText;
    private SearchListAdapter searchListAdapter;
    private RecyclerView.LayoutManager searchListLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
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

        editText = fragmentView.findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        doSearch(editText.getText().toString());
    }

    public void doSearch(final String query){
        DataManager.getInstance(getContext()).getArtworkList(new DataProvider.ArtworkListCompletion() {
            @Override
            public void complete(List<Artwork> artworkList) {
                if(searchListAdapter != null) {
                    Log.d("search", "s = " + artworkList.size());
                    searchListAdapter.setFilteredArtworks(new ArrayList<>(artworkList), query);
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

        public void setFilteredArtworks(ArrayList<Artwork> artworks, String query) {
            if(this.artworks != null) {
                this.artworks.clear();
            }
            else {
                this.artworks = new ArrayList<>();
            }
            for(int i = 0; i < artworks.size(); i++){
                if(artworks.get(i).getTitle().toLowerCase().contains(query.toLowerCase())){
                    this.artworks.add(artworks.get(i));
                }
            }
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
            viewHolder.artwork = artwork;
        }

        @Override
        public int getItemCount() {
            if(artworks == null) {
                return 0;
            }
            return artworks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public NetworkImageView searchImage;
            public TextView artworkTitle;
            public Artwork artwork;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                searchImage = view.findViewById(R.id.list_image);
                artworkTitle = view.findViewById(R.id.list_title);
            }

            @Override
            public void onClick(View v) {
                if(artwork != null) {
                    Intent intent = new Intent(getActivity(), ArtworkDetailActivity.class);
                    intent.putExtra(Artwork.KEY_NAME_ID, artwork.getId());
                    startActivity(intent);
                }
            }
        }
    }



}
