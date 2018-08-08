package com.avanade.artmachina.app.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.android.volley.toolbox.NetworkImageView;
import com.avanade.artmachina.R;
import com.avanade.artmachina.app.models.Artwork;
import com.avanade.artmachina.app.models.Comment;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArtworkDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView detailList;
    ArtworkDetailAdapter artworkDetailAdapter;
    String artworkId = "";
    EditText newCommentEditText;
    ImageButton addCommentButton;
    Target target;
    View sourceView;
    View processedView;
    int animationDuration;
    Animation animCrossFadeIn, animCrossFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getIntent().hasExtra(Artwork.KEY_NAME_ID)) {
            onBackPressed();
            return;
        }
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

        artworkId = getIntent().getStringExtra(Artwork.KEY_NAME_ID);
        DataManager.getInstance(this).getArtwork(artworkId, new DataProvider.ArtworkCompletion() {
            @Override
            public void complete(final Artwork artwork) {
                artworkDetailAdapter.setArtwork(artwork);

                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Picasso.get().load(String.valueOf(artwork.getProcessedImageUrl())).into(new Target() {
                            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("image/*");
                                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                startActivity(Intent.createChooser(i, "Share Image"));
                            }

                            @Override public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
                            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
                        });

                        return false;
                    }
                });
            }

            @Override
            public void failure(HttpResponseError error) {

            }
		});

        newCommentEditText = findViewById(R.id.new_comment);
        addCommentButton = findViewById(R.id.add_comment);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = newCommentEditText.getText().toString();
                if(comment.length() == 0) {
                    Toast.makeText(ArtworkDetailActivity.this, "No comment was entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(DataManager.getInstance(ArtworkDetailActivity.this).isLoggedIn()) {
                    DataManager.getInstance(ArtworkDetailActivity.this).addComment(artworkId, comment, new DataProvider.EmptyCompletion() {
                        @Override
                        public void complete() {
                            updateComments();
                            newCommentEditText.setText("");
                            View view = ArtworkDetailActivity.this.getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        }

                        @Override
                        public void failure(HttpResponseError error) {
                            Toast.makeText(ArtworkDetailActivity.this, "There was an error adding your comment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to make a comment", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void animate() {
        sourceView.setAlpha(0f);
        sourceView.setVisibility(View.VISIBLE);

        sourceView.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        processedView.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        processedView.setVisibility(View.GONE);
                    }
                });
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

    @Override
    public void onResume() {
        super.onResume();
        DataManager.getInstance(this).getArtwork(artworkId, new DataProvider.ArtworkCompletion() {
            @Override
            public void complete(Artwork artwork) {
                artworkDetailAdapter.setArtwork(artwork);
            }

            @Override
            public void failure(HttpResponseError responseError) {
                Log.d("Network", responseError.getErrorCode() + " " + responseError.getErrorMessage());
                Toast.makeText(ArtworkDetailActivity.this, "There was an error fetching Artwork details", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        updateComments();
    }

    private void updateComments() {
        DataManager.getInstance(this).getComments(artworkId, new DataProvider.CommentListCompletion() {
            @Override
            public void complete(List<Comment> commentList) {
                Log.d("comments", "" + commentList.size());
                artworkDetailAdapter.setComments(new ArrayList<>(commentList));
            }

            @Override
            public void failure(HttpResponseError error) {
                Toast.makeText(ArtworkDetailActivity.this, "There was an error fetching Artwork comments", Toast.LENGTH_SHORT).show();
                artworkDetailAdapter.setComments(null);
            }
        });
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
                headerViewHolder.setButtonRatingStars(artwork.getMyRating());
            }
            else if(viewType == VIEW_TYPE_COMMENT) {
                CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
                commentViewHolder.setCommentor(comments.get(position - 1).getReviewerName());
                commentViewHolder.setComment(comments.get(position - 1).getContent());
            }
        }

        @Override
        public int getItemCount() {
            if(artwork != null && comments != null) {
                return comments.size() + 1;
            }
            return 0;
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder implements TabLayout.OnTabSelectedListener, View.OnClickListener {
            private NetworkImageView mainImage, mainImage2;
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

            public HeaderViewHolder(View view, final Artwork artwork) {
                super(view);
                mainImage = view.findViewById(R.id.main_image);
                mainImage2 = view.findViewById(R.id.main_image2) ;
                tabLayout = view.findViewById(R.id.tab_layout);
                tabLayout.addOnTabSelectedListener(this);
                tabLayout.getTabAt(2).select();
                author = view.findViewById(R.id.author);
                bookmarkButton = view.findViewById(R.id.bookmarkButton);
                bookmarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        artwork.setBookmarked(!artwork.isBookmarked());
                        DataManager.getInstance(ArtworkDetailActivity.this).updateBookmark(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                setIsBookmarked(artwork.isBookmarked());
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to bookmark", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("bookmark", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setBookmarked(!artwork.isBookmarked());
                            }
                        });
                    }
                });
                ratingStar1 = view.findViewById(R.id.rating_star1);
                ratingStar2 = view.findViewById(R.id.rating_star2);
                ratingStar3 = view.findViewById(R.id.rating_star3);
                ratingStar4 = view.findViewById(R.id.rating_star4);
                ratingStar5 = view.findViewById(R.id.rating_star5);
                ratingCount = view.findViewById(R.id.rating_text);
                description = view.findViewById(R.id.description);
                ratingButton1 = view.findViewById(R.id.rating_button1);
                ratingButton1.setOnClickListener(this);
                ratingButton2 = view.findViewById(R.id.rating_button2);
                ratingButton2.setOnClickListener(this);
                ratingButton3 = view.findViewById(R.id.rating_button3);
                ratingButton3.setOnClickListener(this);
                ratingButton4 = view.findViewById(R.id.rating_button4);
                ratingButton4.setOnClickListener(this);
                ratingButton5 = view.findViewById(R.id.rating_button5);
                ratingButton5.setOnClickListener(this);
                this.artwork = artwork;
            }

            public void setMainImage(URL url) {
                mainImage.setImageUrl(url.toString(), DataManager.getInstance(ArtworkDetailActivity.this).getImageLoader());
            }

            public void setMainImageTwo(URL url) {
                mainImage2.setImageUrl(url.toString(), DataManager.getInstance(ArtworkDetailActivity.this).getImageLoader());
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

            // set the star images based on rating amount
            public void setButtonRatingStars(double rating) {
                int roundedRating = (int)Math.floor(rating);
                if(roundedRating == 1) {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 2) {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 3) {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating == 4) {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_border_24px);
                }
                else if(roundedRating >= 5) {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_24px);
                }
                else {
                    ratingButton1.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton2.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton3.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton4.setImageResource(R.drawable.ic_baseline_star_border_24px);
                    ratingButton5.setImageResource(R.drawable.ic_baseline_star_border_24px);
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

            //Four tabs for specific artwork
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                if(artwork != null) {
                    ViewFlipper viewSwitcher = findViewById(R.id.switcher);


                    //ViewSwitcher viewSwitcher = findViewById(R.id.switcher);
                    //viewSwitcher.setDisplayedChild(0);
                    switch (tab.getPosition()) {
                        case 0:
                            setMainImage(artwork.getSourceImageUrl());
                            viewSwitcher.setOnClickListener(null);
                            break;
                        case 1:
                            setMainImage(artwork.getReferenceImageUrl());
                            viewSwitcher.setOnClickListener(null);
                            break;
                        case 2:
                            setMainImage(artwork.getProcessedImageUrl());
                            viewSwitcher.setOnClickListener(null);
                            break;
                        case 3:
                            setMainImage(artwork.getSourceImageUrl());
                            setMainImageTwo(artwork.getProcessedImageUrl());

                            viewSwitcher.startFlipping();
                            viewSwitcher.setFlipInterval(2000);
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

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rating_button1:
                        final double prevRating = artwork.getRating();
                        artwork.setMyRating(1);
                        DataManager.getInstance(ArtworkDetailActivity.this).updateRating(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                Log.d("rate", "success");
                                setButtonRatingStars(1);
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to rate", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("rate", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setMyRating(prevRating);
                                setButtonRatingStars(0);
                            }
                        });
                        break;
                    case R.id.rating_button2:
                        final double prevRating2 = artwork.getRating();
                        artwork.setMyRating(2);
                        DataManager.getInstance(ArtworkDetailActivity.this).updateRating(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                Log.d("rate", "success");
                                setButtonRatingStars(2);
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to rate", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("rate", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setMyRating(prevRating2);
                                setButtonRatingStars(0);
                            }
                        });
                        break;
                    case R.id.rating_button3:
                        final double prevRating3 = artwork.getRating();
                        artwork.setMyRating(3);
                        DataManager.getInstance(ArtworkDetailActivity.this).updateRating(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                Log.d("rate", "success");
                                setButtonRatingStars(3);
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to rate", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("rate", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setMyRating(prevRating3);
                                setButtonRatingStars(0);
                            }
                        });
                        break;
                    case R.id.rating_button4:
                        final double prevRating4 = artwork.getRating();
                        artwork.setMyRating(4);
                        DataManager.getInstance(ArtworkDetailActivity.this).updateRating(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                Log.d("rate", "success");
                                setButtonRatingStars(4);
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to rate", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("rate", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setMyRating(prevRating4);
                                setButtonRatingStars(0);
                            }
                        });
                        break;
                    case R.id.rating_button5:
                        final double prevRating5 = artwork.getRating();
                        artwork.setMyRating(5);
                        DataManager.getInstance(ArtworkDetailActivity.this).updateRating(artwork, new DataProvider.EmptyCompletion() {
                            @Override
                            public void complete() {
                                Log.d("rate", "success");
                                setButtonRatingStars(5);
                            }

                            @Override
                            public void failure(HttpResponseError error) {
                                if(error.getErrorCode() == 401) {
                                    Toast.makeText(ArtworkDetailActivity.this, "Please log in to rate", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ArtworkDetailActivity.this, LogInActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Log.d("rate", error.getErrorCode() + " " + error.getErrorMessage());
                                }
                                artwork.setMyRating(prevRating5);
                                setButtonRatingStars(5);
                            }
                        });
                        break;
                }
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
