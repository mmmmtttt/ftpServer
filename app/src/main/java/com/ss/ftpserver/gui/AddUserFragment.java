package com.ss.ftpserver.gui;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ss.ftpserver.R;
import com.ss.ftpserver.databinding.FragmentAddUserBinding;

public class AddUserFragment extends DialogFragment {
    private FragmentAddUserBinding binding;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        navController = findNavController(getActivity().findViewById(R.id.fragment_main));
        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        //修改窗口出现的位置
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();

            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btSaveAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传递新增用户数据
                String[] newUser = new String[3];
                newUser[0] = binding.inputUsername.getText().toString();
                newUser[1] = binding.inputPass.getText().toString();
                newUser[2] = binding.toggleLegal.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putStringArray("newUser", newUser);
                navController.navigate(R.id.action_addUserFragment_to_usersFragment,bundle);
            }
        });
    }
}