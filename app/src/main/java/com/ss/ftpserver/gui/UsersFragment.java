package com.ss.ftpserver.gui;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ss.ftpserver.R;
import com.ss.ftpserver.ftpService.Settings;
import com.ss.ftpserver.ftpService.User;

/**
 * A fragment representing a list of Users.
 */
public class UsersFragment extends Fragment {
    NavController navController;
    UserRecyclerViewAdapter userAdapter = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);//设置app bar上的添加按钮
        navController = findNavController(getActivity().findViewById(R.id.fragment_main));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.user_list);
        if (userAdapter == null){
            userAdapter = new UserRecyclerViewAdapter(Settings.getUsers());
        }
        recyclerView.setAdapter(userAdapter);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.add_user_menu,menu);//设置app bar上的添加按钮
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController.navigate(R.id.action_usersFragment_to_addUserFragment);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments()!=null){
            String[] newUser = (String[]) getArguments().get("newUser");
            boolean isLegal = (newUser[2].equals("legal"));
            User user = new User(newUser[0],newUser[1],isLegal);
            userAdapter.addUser(user);
        }
    }
}