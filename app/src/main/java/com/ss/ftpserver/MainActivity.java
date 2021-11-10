package com.ss.ftpserver;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //将App bar与NavController绑定，当NavController完成Fragment切换时，系统会自动在App bar中完成一些常见操作
        NavController navController = findNavController(findViewById(R.id.fragment_main));
        setupActionBarWithNavController(this,navController);
    }

    //使用toolbar的后退按钮后退
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(findViewById(R.id.fragment_main));
        return navController.navigateUp()||super.onSupportNavigateUp();
    }
}