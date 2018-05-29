package com.avanade.artmachina.app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.avanade.artmachina.R;
import com.avanade.artmachina.app.fragments.ArtworkFragment;
import com.avanade.artmachina.app.fragments.BookmarksFragment;
import com.avanade.artmachina.app.fragments.SearchFragment;
import com.avanade.artmachina.app.fragments.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigation;
    ViewPager viewPager;

    /* Activity Lifecycle Methods */

    // called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        navigation = findViewById(R.id.gallery_navigation);
        viewPager = findViewById(R.id.view_pager);
        navigation.setOnNavigationItemSelectedListener(this);
        setupViewPager();
    }

    /* Private Methods */

    // sets up bottom list fragment navigation
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArtworkFragment artworkFragment = new ArtworkFragment();
        SearchFragment searchFragment = new SearchFragment();
        BookmarksFragment bookmarksFragment = new BookmarksFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
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

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

}
