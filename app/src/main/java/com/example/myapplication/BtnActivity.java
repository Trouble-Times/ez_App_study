package com.example.myapplication;

import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BtnActivity extends AppCompatActivity {
    // 加载native库
    static {
        System.loadLibrary("native-lib");
    }
    
    // 修改native方法声明，添加三个int参数
    public native int add(int a, int b, int c);

    // 添加Java减法方法
    private int Sub(int a, int b) {
        return a - b;
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_btn);
        
        Button btnJump = findViewById(R.id.btn_click_native);
        btnJump.setOnClickListener(v -> {
            // 调用native方法并传入参数1,2,3
            int result = add(1, 2, 3);
            Toast.makeText(BtnActivity.this, 
                "Native返回结果: " + result, 
                Toast.LENGTH_SHORT).show();
        });

        // 添加Java方法按钮点击事件
        Button btnJava = findViewById(R.id.btn_click_java);
        btnJava.setOnClickListener(v -> {
            int result = Sub(3, 1); // 调用Java方法
            Toast.makeText(BtnActivity.this,
                "Java返回结果: " + result,
                Toast.LENGTH_SHORT).show();
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