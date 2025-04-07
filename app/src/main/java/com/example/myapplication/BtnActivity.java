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

    // 新增内部类实现乘法
    public static class Multiplier {
        private int baseValue;
        
        // 构造函数
        public Multiplier(int baseValue) {
            this.baseValue = baseValue;
        }
        
        // 实例方法，使用构造函数设置的baseValue
        public int multiplyWithBase(int b) {
            return baseValue * b;
        }
        
        // 重载方法1：两个int参数
        public static int multiply(int a, int b) {
            return a * b * 2;
        }
        
        // 重载方法2：三个int参数
        public static int multiply(int a, int b, int c) {
            return a * b * c * 3;
        }
        
    }

    // 构造函数
    public class User {
        private String name;
        
        public User(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }

    // 接口方法
    public interface Calculator {
        int calculate(int a, int b);
    }

    public class SimpleCalculator implements Calculator {
        @Override 
        public int calculate(int a, int b) {
            return a + b * 2;
        }
    }

    // 添加异步任务方法
    private int asyncTask() {
        new Thread(() -> {
            android.util.Log.d("ThreadDemo", "后台线程执行");}).start();
        return Thread.activeCount();
    }



    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_btn);
        
        // Native方法调用
        findViewById(R.id.btn_click_native).setOnClickListener(v -> {
            int result = add(1, 2, 3);
            Toast.makeText(this, getString(R.string.native_return_string) + result, Toast.LENGTH_SHORT).show();
        });
    
        // Java方法调用
        findViewById(R.id.btn_click_java).setOnClickListener(v -> {
            int result = Sub(3, 1);
            Toast.makeText(this, "Java返回结果: " + result, Toast.LENGTH_SHORT).show();
        });
    
        // 内部类方法
        findViewById(R.id.btn_InnerClass).setOnClickListener(v -> {
            int result = Multiplier.multiply(2, 3);
            Toast.makeText(this, "内部类乘法结果: " + result, Toast.LENGTH_SHORT).show();
        });
    
        // 构造函数
        findViewById(R.id.btn_OnCreate).setOnClickListener(v -> {
            User user = new User("测试用户");
            Toast.makeText(this, "构造函数创建用户: " + user.getName(), Toast.LENGTH_SHORT).show();
        });
    
        // 重载函数
        findViewById(R.id.btn_Reload).setOnClickListener(v -> {
            int result1 = Multiplier.multiply(2, 3);
            int result2 = Multiplier.multiply(2, 3, 4);
            Toast.makeText(this, 
                "重载方法结果: " + result1 + " 和 " + result2, 
                Toast.LENGTH_SHORT).show();
        });
    
        // 接口实现
        findViewById(R.id.btn_Implements).setOnClickListener(v -> {
            Calculator calculator = new SimpleCalculator();
            int result = calculator.calculate(2, 3);
            Toast.makeText(this, "接口实现结果: " + result, Toast.LENGTH_SHORT).show();
        });
    
        // 多线程
        findViewById(R.id.btn_Thread).setOnClickListener(v -> {
            int count = asyncTask();
            Toast.makeText(this, "多线程任务已启动,现在已有" + count + "个线程", Toast.LENGTH_SHORT).show();
        });
    
        // Hook系统Toast
        findViewById(R.id.btn_Toast).setOnClickListener(v -> {
            Toast.makeText(this, "原始Toast消息", Toast.LENGTH_SHORT).show();
        });
    
        // 底部导航栏
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