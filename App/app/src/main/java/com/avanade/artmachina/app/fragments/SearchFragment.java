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

import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private ArrayList<Artwork> artworks;
    private RecyclerView searchList;
    private RecyclerView.Adapter searchListAdapter;
    private RecyclerView.LayoutManager searchListLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artworks = new ArrayList<>();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        searchList = fragmentView.findViewById(R.id.search_list);
        searchList.setHasFixedSize(true);
        searchListLayoutManager = new LinearLayoutManager(getContext());
        searchList.setLayoutManager(searchListLayoutManager);
        searchListAdapter = new SearchListAdapter(artworks);
        searchList.setAdapter(searchListAdapter);
        //int spacingInPixels = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        //galleryList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return fragmentView;
    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
        private ArrayList<Artwork> artworks;

        public SearchListAdapter(ArrayList<Artwork> artworks) {
            this.artworks = artworks;
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
            viewHolder.searchImage.setDefaultImageResId(R.drawable.ic_baseline_star_border_24px);
            viewHolder.searchImage.setImageUrl("https://avanadeprojectstorage.blob.core.windows.net/art-machina/pro_Self-Portrait_1889.png", DataManager.getInstance(getActivity()).getImageLoader());
            viewHolder.artworkTitle.setText(artwork.getTitle());
        }

        @Override
        public int getItemCount() {
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
