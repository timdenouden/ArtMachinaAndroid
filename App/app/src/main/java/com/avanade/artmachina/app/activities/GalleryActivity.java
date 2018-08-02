package com.avanade.artmachina.app.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.avanade.artmachina.R;
import com.avanade.artmachina.app.fragments.ArtworkFragment;
import com.avanade.artmachina.app.fragments.BookmarksFragment;
import com.avanade.artmachina.app.fragments.SearchFragment;
import com.avanade.artmachina.app.fragments.SettingsFragment;
import com.avanade.artmachina.app.views.IndexableBottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "Gallery";
    public static final int PERMISSION_REQUEST_CODE_INTERNET = 1;

    Toolbar toolbar;
    IndexableBottomNavigationView navigation;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    SettingsFragment settingsFragment;

    /* Activity Lifecycle Methods */

    // called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.gallery_navigation);
        viewPager = findViewById(R.id.view_pager);
        navigation.setOnNavigationItemSelectedListener(this);
        setupViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.gallery_filter, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_CODE_INTERNET);
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_INTERNET:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    /* Private Methods */

    // sets up bottom list fragment navigation
    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArtworkFragment artworkFragment = new ArtworkFragment();
        SearchFragment searchFragment = new SearchFragment();
        BookmarksFragment bookmarksFragment = new BookmarksFragment();
        settingsFragment = new SettingsFragment();
        adapter.addFragment(artworkFragment);
        adapter.addFragment(searchFragment);
        adapter.addFragment(bookmarksFragment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
    }

    /* Implemented Methods*/

    // called when user taps on bottom navigation bar item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery_artwork:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.gallery_search:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.gallery_bookmarks:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.gallery_settings:
                viewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    /* Private Classes */

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if(navigation.getSelectedIndex() != position) {
                navigation.setSelectedIndex(position);
            }
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

}
