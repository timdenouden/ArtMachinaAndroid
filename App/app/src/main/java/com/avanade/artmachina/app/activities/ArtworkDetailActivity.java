package com.avanade.artmachina.app.activities;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.Comment;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;

import java.net.URL;
import java.util.ArrayList;

public class ArtworkDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView detailList;
    ArtworkDetailAdapter artworkDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_detail);
        toolbar = findViewById(R.id.artwork_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorTextLight), PorterDuff.Mode.SRC_ATOP);

        detailList = findViewById(R.id.detail_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        detailList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        detailList.addItemDecoration(dividerItemDecoration);
        artworkDetailAdapter = new ArtworkDetailAdapter();
        detailList.setAdapter(artworkDetailAdapter);


        Log.d(this.getClass().getSimpleName(), "starting");
        DataManager.getInstance(this).getArtwork(1, new DataProvider.ArtworkCompletion() {
            @Override
            public void complete(Artwork artwork) {
                artworkDetailAdapter.setArtwork(artwork);
            }

            @Override
            public void failure(String failureMessage) {

            }
        });

        // This needs to be wrapped up in the DataManager
        ArrayList<Comment> debugComments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setReviewerName("Tester McTesterson");
        comment.setContent("Wow that is neat!");
        debugComments.add(comment);
        Comment comment2 = new Comment();
        comment2.setReviewerName("Testerson McTester");
        comment2.setContent("I too think this is neat!");
        debugComments.add(comment2);
        artworkDetailAdapter.setComments(debugComments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artwork_detail_share, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class ArtworkDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_COMMENT = 1;

        private static final int IMAGE_TYPE_SOURCE = 0;
        private static final int IMAGE_TYPE_REFERENCE = 1;
        private static final int IMAGE_TYPE_PROCESSED = 2;
        private static final int IMAGE_TYPE_ANIMATION = 3;

        private Artwork artwork;
        private ArrayList<Comment> comments;

        public ArtworkDetailAdapter() {

        }

        public ArtworkDetailAdapter(Artwork artwork, ArrayList<Comment> comments) {
            this.artwork = artwork;
            this.comments = comments;
        }

        public void setArtwork(Artwork artwork) {
            this.artwork = artwork;
            notifyDataSetChanged();
        }

        public void setComments(ArrayList<Comment> comments) {
            this.comments = comments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                return VIEW_TYPE_HEADER;
            }
            return VIEW_TYPE_COMMENT;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            if(viewType == VIEW_TYPE_HEADER) {
                View view = LayoutInflater.from(ArtworkDetailActivity.this).inflate(R.layout.artwork_detail_head, parent, false);
                viewHolder = new HeaderViewHolder(view, artwork);
            }
            else if(viewType == VIEW_TYPE_COMMENT) {
                View view = LayoutInflater.from(ArtworkDetailActivity.this).inflate(R.layout.list_item_comment, parent, false);
                viewHolder = new CommentViewHolder(view);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            if(viewType == VIEW_TYPE_HEADER) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder)holder;
                headerViewHolder.setMainImage(artwork.getProcessedImageUrl());
                headerViewHolder.setAuthor(artwork.getAuthor());
                headerViewHolder.setRatingStars(artwork.getRating());
                headerViewHolder.setRatingCount(artwork.getRatingCount());
                headerViewHolder.setIsBookmarked(artwork.isBookmarked());
                headerViewHolder.setDescription(artwork.getDescription());
            }
            else if(viewType == VIEW_TYPE_COMMENT) {
                CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
                commentViewHolder.setCommentor(comments.get(position - 1).getReviewerName());
                commentViewHolder.setComment(comments.get(position - 1).getContent());
            }
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if(artwork != null) {
                count++;
            }
            if(comments != null) {
                count += comments.size();
            }
            return count;
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder implements TabLayout.OnTabSelectedListener {
            private NetworkImageView mainImage;
            private TabLayout tabLayout;
            private TextView author;
            private ImageButton bookmarkButton;
            private ImageView ratingStar1;
            private ImageView ratingStar2;
            private ImageView ratingStar3;
            private ImageView ratingStar4;
            private ImageView ratingStar5;
            private TextView ratingCount;
            private TextView description;
            private ImageButton ratingButton1;
            private ImageButton ratingButton2;
            private ImageButton ratingButton3;
            private ImageButton ratingButton4;
            private ImageButton ratingButton5;
            private Artwork artwork;

            public HeaderViewHolder(View view, Artwork artwork) {
                super(view);
                mainImage = view.findViewById(R.id.main_image);
                tabLayout = view.findViewById(R.id.tab_layout);
                tabLayout.addOnTabSelectedListener(this);
                tabLayout.getTabAt(2).select();
                author = view.findViewById(R.id.author);
                bookmarkButton = view.findViewById(R.id.bookmarkButton);
                ratingStar1 = view.findViewById(R.id.rating_star1);
                ratingStar2 = view.findViewById(R.id.rating_star2);
                ratingStar3 = view.findViewById(R.id.rating_star3);
                ratingStar4 = view.findViewById(R.id.rating_star4);
                ratingStar5 = view.findViewById(R.id.rating_star5);
                ratingCount = view.findViewById(R.id.rating_text);
                description = view.findViewById(R.id.description);
                ratingButton1 = view.findViewById(R.id.rating_button1);
                ratingButton2 = view.findViewById(R.id.rating_button2);
                ratingButton3 = view.findViewById(R.id.rating_button3);
                ratingButton4 = view.findViewById(R.id.rating_button4);
                ratingButton5 = view.findViewById(R.id.rating_button5);
                this.artwork = artwork;
            }

            public void setMainImage(URL url) {
                mainImage.setImageUrl(url.toString(), DataManager.getInstance(ArtworkDetailActivity.this).getImageLoader());
            }

            public void setAuthor(String authorText) {
                author.setText(authorText);
            }

            // set background of bookmarked button based on isBookmarked value
            public void setIsBookmarked(boolean isBookmarked) {
                if(isBookmarked) {
                    bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24px);
                }
                else {
                    bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_border_24px);
                }
            }

            // set the star images based on rating amount
            public void setRatingStars(double rating) {
                int roundedRating = (int)Math.floor(rating);
                if(roundedRating == 1) {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 2) {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 3) {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 4) {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating >= 5) {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_24px);
                }
                else {
                    ratingStar1.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar2.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingStar5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
            }

            public void setRatingCount(int count) {
                if(count > 1) {
                    ratingCount.setText(count + " ratings");
                }
                else if(count == 1) {
                    ratingCount.setText(count + " rating");
                }
                else {
                    ratingCount.setText("No ratings");
                }
            }

            public void setDescription(String descriptionText) {
                description.setText(descriptionText);
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(artwork != null) {
                    switch (tab.getPosition()) {
                        case 0:
                            setMainImage(artwork.getSourceImageUrl());
                            break;
                        case 1:
                            setMainImage(artwork.getReferenceImageUrl());
                            break;
                        case 2:
                            setMainImage(artwork.getProcessedImageUrl());
                            break;
                        case 3:
                            setMainImage(artwork.getProcessedImageUrl());
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            private TextView commenter;
            private TextView comment;

            public CommentViewHolder(View view) {
                super(view);
                commenter = view.findViewById(R.id.commenter);
                comment = view.findViewById(R.id.comment);
            }

            public void setCommentor(String commenter) {
                this.commenter.setText(commenter);
            }

            public void setComment(String comment) {
                this.comment.setText(comment);
            }
        }
    }
}
