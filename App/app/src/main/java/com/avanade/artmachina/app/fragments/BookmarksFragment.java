package com.avanade.artmachina.app.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.activities.ArtworkDetailActivity;
import com.avanade.artmachina.app.activities.LogInActivity;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment {
    private TextView displayText;
    private RecyclerView bookmarkList;
    private BookmarkListAdapter adapter;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bookmarks, container, false);

        displayText = view.findViewById(R.id.displayText);
        bookmarkList = view.findViewById(R.id.boomarkList);
        bookmarkList.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkList.setHasFixedSize(true);
        adapter = new BookmarkListAdapter();
        bookmarkList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayText.setText("Loading Bookmarks...");
        DataManager.getInstance(getContext()).getBookmarkList(new DataProvider.ArtworkListCompletion() {
            @Override
            public void complete(List<Artwork> artworkList) {
                if(adapter != null) {
                    adapter.setArtworkList(new ArrayList<>(artworkList));
                }
                if(artworkList.size() > 0) {
                    displayText.setText("");
                }
                else {
                    displayText.setText("No Bookmarks Set");
                }
            }

            @Override
            public void failure(HttpResponseError error) {
                if(adapter != null) {
                    adapter.setArtworkList(null);
                }
                if(error.getErrorCode() >= 400 && error.getErrorCode() < 500) {
                    displayText.setText("Not Logged In");
                }
                else {
                    displayText.setText("No Bookmarks Found");
                }
            }
        });
    }

    public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.ViewHolder> {

        private ArrayList<Artwork> artworks;

        public BookmarkListAdapter() { }

        public void setArtworkList(ArrayList<Artwork> list) {
            artworks = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.search_item_artwork, parent, false);
            BookmarkListAdapter.ViewHolder viewHolder = new BookmarkListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Artwork artwork = artworks.get(position);
            viewHolder.bookmarkImage.setImageUrl(artwork.getProcessedImageUrl().toString(), DataManager.getInstance(getActivity()).getImageLoader());
            viewHolder.artworkTitle.setText(artwork.getTitle());
            viewHolder.artwork = artwork;

        }

        @Override
        public int getItemCount() {
            if(this.artworks != null) {
                return this.artworks.size();
            }
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public NetworkImageView bookmarkImage;
            public TextView artworkTitle;
            public Artwork artwork;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                bookmarkImage = view.findViewById(R.id.list_image);
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
