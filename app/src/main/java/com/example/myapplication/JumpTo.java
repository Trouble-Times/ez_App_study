package com.example.myapplication;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.myapplication.utils.NavigationHelper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class JumpTo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_to);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, navView, R.id.nav_play);

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 确保跳转页面的导航栏选中状态正确
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.nav_play);
    }
}