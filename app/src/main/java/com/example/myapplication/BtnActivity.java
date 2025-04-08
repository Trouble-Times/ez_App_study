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

    // 静态内部类 + 被调用过 + 重载函数
    public static class StaticUsed {
        static private String name1 = "静态变量名字_StaticUsed";
        private String name2 = "成员变量名字_StaticUsed";

        // 构造函数
        public StaticUsed(String name2) {
            this.name2 = name2;
        }
        
        // 普通方法
        public String getName() {
            return name1 + ", " + name2;  // 修改这里，直接使用类自己的静态变量
        }
        
        // 重载方法1：两个int参数
        public static int mul(int a, int b) {
            return a * b * 2;
        }
        
        // 重载方法2：三个int参数
        public static int mul(int a, int b, int c) {
            return a * b * c * 3;
        }

    }

    // 静态内部类 且 从来没被调用过
    public static class StaticNotUsed {
        private String name = "成员变量名字_StaticNotUsed";
        // 构造函数
        public StaticNotUsed(String name) {
            this.name = name;
        }
        
        // 普通方法
        public String getName() {
            return this.name;
        }
                
    }

    // 非静态内部类 且 被调用过
    public class CheckUsed {
        private int threshold = 10;
        
        public String check(int value) {
            return "value: " + value + " " + "private_Data: " + this.threshold;
        }
        
        public void setThreshold(int newThreshold) {
            this.threshold = newThreshold;
        }
    }

    // 非静态内部类 且 从来没被调用过 , 尝试hook调用里面的 构造 和 普通方法
    public class CheckNotUsed  {
        private int threshold = 10;
        
        public String check(int value) {
            return "value: " + value + " " + "private_Data: " + this.threshold;
        }
        
        public void setThreshold(int newThreshold) {
            this.threshold = newThreshold;
        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_btn);
        
        // Native方法调用
        findViewById(R.id.btn_click_native).setOnClickListener(v -> {
            int result = add(1, 2, 3);
            Toast.makeText(this, "Native 返回结果: " + getString(R.string.native_return_string) + result, Toast.LENGTH_SHORT).show();
        });
    
        // Java方法调用
        findViewById(R.id.btn_click_java).setOnClickListener(v -> {
            int result = Sub(3, 1);
            Toast.makeText(this, "Java返回结果: " + result, Toast.LENGTH_SHORT).show();
        });
        
        // 接口实现
        findViewById(R.id.btn_Implements).setOnClickListener(v -> {
            Calculator calculator = new SimpleCalculator();
            int result = calculator.calculate(2, 3);
            Toast.makeText(this, "接口实现结果: " + result, Toast.LENGTH_SHORT).show();
        });

        // 静态内部类 + 普通方法 + 构造函数 + 静态变量
        findViewById(R.id.btn_Inner_OnCreate).setOnClickListener(v -> {
            StaticUsed StaticUsed = new StaticUsed("我输入的名字");
            Toast.makeText(this, "静态内部类 普通方法 : " 
            + StaticUsed.getName(), Toast.LENGTH_SHORT).show();
        });

        // 静态内部类 + 重载函数
        findViewById(R.id.btn_Inner_Reload).setOnClickListener(v -> {
            int result1 = StaticUsed.mul(2, 3);
            int result2 = StaticUsed.mul(2, 3, 4);
            Toast.makeText(this, 
                "静态内部类 重载结果为: " + result1 + " 和 " + result2, 
                Toast.LENGTH_SHORT).show();
        });
    
        // 非静态内部类 构造函数 + 静态变量
        findViewById(R.id.btn_NotInner_Not).setOnClickListener(v -> {
            CheckUsed checkUsed = new CheckUsed();
            Toast.makeText(this,
                "非静态内部类 " + checkUsed.check(10),
                Toast.LENGTH_SHORT).show();
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
