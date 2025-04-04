package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SaveDataAndFunc extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data_and_func);

        // 使用工具类设置导航栏
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, navView, R.id.nav_wallpaper);

        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(v -> {
            Toast.makeText(SaveDataAndFunc.this, "你按下了保存按钮", Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.nav_wallpaper);
    }
}