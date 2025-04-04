package com.example.myapplication;

import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BtnActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_btn);
        
        // 获取按钮并设置点击事件
        Button btnJump = findViewById(R.id.btn_click);
        btnJump.setOnClickListener(v -> {
            Toast.makeText(BtnActivity.this, "你点击了按钮", Toast.LENGTH_SHORT).show();
        });

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, navView, R.id.nav_home);


    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回Activity时更新导航栏选中状态
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.nav_home);
    }
}