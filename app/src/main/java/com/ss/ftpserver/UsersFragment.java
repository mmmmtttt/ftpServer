package com.ss.ftpserver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Users.
 */
public class UsersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setAdapter(new UserRecyclerViewAdapter(Settings.getUsers()));
        //TODO:处理每个edit和delete button
        return view;
    }
}