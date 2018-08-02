package com.avanade.artmachina.app.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avanade.artmachina.R;
import com.avanade.artmachina.app.activities.LogInActivity;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;
import com.avanade.artmachina.app.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout userInfoContainer;
    private TextView loginItem;
    private EditText firstName;
    private EditText lastName;
    private EditText title;
    private EditText company;
    private Button save;
    private TextView avanade;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_logged_in, container, false);

        swipeRefreshLayout = view.findViewById(R.id.settings_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserAndUpdateUI();
            }
        });

        userInfoContainer = view.findViewById(R.id.user_info_container);
        firstName = view.findViewById(R.id.settings_first_name);
        lastName = view.findViewById(R.id.settings_last_name);
        title = view.findViewById(R.id.settings_title);
        company = view.findViewById(R.id.settings_company);
        save = view.findViewById(R.id.settings_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            User user = DataManager.getInstance(getActivity()).getUser();
            user.setFirstName(firstName.getText().toString());
            user.setLastName(lastName.getText().toString());
            user.setTitle(title.getText().toString());
            user.setCompany(company.getText().toString());
            DataManager.getInstance(getActivity()).updateProfile(user, new DataProvider.ProfileCompletion() {
                @Override
                public void complete(User profile) {
                    DataManager.getInstance(getActivity()).setUser(profile);
                    Toast.makeText(getActivity(), "User profile saved.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(HttpResponseError error) {
                    Toast.makeText(getActivity(), "Could not update profile.", Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

        loginItem = view.findViewById(R.id.login);

        avanade = view.findViewById(R.id.avanade);
        avanade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.avanade.com/en/technologies/artificial-intelligence"));
                startActivity(browserIntent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        fetchUserAndUpdateUI();
    }

    public void fetchUserAndUpdateUI() {
        DataManager.getInstance(getActivity()).getProfile(new DataProvider.ProfileCompletion() {
            @Override
            public void complete(User profile) {
                updateUIWithUser(profile);
                DataManager.getInstance(getActivity()).setUser(profile);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(HttpResponseError error) {
                updateUIAnon();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void updateUIWithUser(User user) {
        userInfoContainer.setVisibility(View.VISIBLE);
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        title.setText(user.getTitle());
        company.setText(user.getCompany());

        loginItem.setText("Log Out");
        loginItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance(getActivity()).logOut();
                updateUIAnon();
            }
        });
    }

    public void updateUIAnon() {
        userInfoContainer.setVisibility(View.GONE);
        firstName.setText("");
        lastName.setText("");
        title.setText("");
        company.setText("");

        loginItem.setText("Log In");
        loginItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
