package com.ss.ftpserver.gui;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ss.ftpserver.R;

public class MainActivity extends AppCompatActivity {
    NavController navController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = findNavController(findViewById(R.id.fragment_main));
        //将App bar与NavController绑定，当NavController完成Fragment切换时，系统会自动在App bar中完成一些常见操作
        //把三个fragment都设定成顶层目的地,不在app bar上显示向上导航的图标
        AppBarConfiguration appBarConf = new AppBarConfiguration.Builder(R.id.homeFragment,R.id.settingsFragment,R.id.usersFragment).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConf);
        //将底部导航栏与NavController绑定
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    //使用toolbar的后退按钮后退
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(findViewById(R.id.fragment_main));
        return navController.navigateUp()||super.onSupportNavigateUp();
    }
}