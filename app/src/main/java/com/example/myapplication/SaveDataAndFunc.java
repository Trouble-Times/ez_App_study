package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.utils.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SaveDataAndFunc extends AppCompatActivity {
    private EditText editTextInput;
    private Button btnOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data_and_func);
        
        // 初始化视图
        editTextInput = findViewById(R.id.EditText_input);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        Button saveButton = findViewById(R.id.btn_save);
        btnOutput = findViewById(R.id.btn_output);

        // 使用工具类设置导航栏
        NavigationHelper.setupBottomNavigation(this, navView, R.id.nav_wallpaper);
        editTextInput = findViewById(R.id.EditText_input);

        saveButton.setOnClickListener(v -> {
            String inputText = editTextInput.getText().toString();
            if (!inputText.isEmpty()) {
                saveToFile(inputText);
                Toast.makeText(this, "数据保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请输入内容再保存", Toast.LENGTH_SHORT).show();
            }
        });

        btnOutput.setOnClickListener(v -> {
            String savedData = readFromFile();
            if (savedData != null) {
                Toast.makeText(this, "保存的数据: " + savedData, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "没有找到保存的数据", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String readFromFile() {
        try (FileInputStream fis = openFileInput("data_my_input")) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveToFile(String data) {
        try (FileOutputStream fos = openFileOutput("data_my_input", MODE_PRIVATE)) {
            fos.write(data.getBytes());
            Log.d("FileSave", "文件已保存到: " + getFilesDir().getAbsolutePath() + "/data_my_input");
            // 文件已保存到: /data/user/0/com.example.myapplication/files/data_my_input
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setSelectedItemId(R.id.nav_wallpaper);
    }
}