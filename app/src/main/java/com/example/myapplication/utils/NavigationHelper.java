package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplication.BtnActivity;
import com.example.myapplication.JumpTo;
import com.example.myapplication.R;
import com.example.myapplication.SaveDataAndFunc;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.ActivityManager;
import java.util.List;

public class NavigationHelper {
    public static void setupBottomNavigation(
            Context context, 
            BottomNavigationView navView, 
            int selectedItemId
            /* 示例传参:
             * BottomNavigationView navView = findViewById(R.id.bottom_navigation);
             * NavigationHelper.setupBottomNavigation(this, navView, R.id.nav_wallpaper); // 传入 当前活动的类，导航id，当前页面id
             */
    ) {
        navView.setSelectedItemId(selectedItemId);// 根据当前页面id，设置当前导航栏选中项
        navView.setOnItemSelectedListener(item -> { // 监听导航栏点击事件, 点击后getItemId()获取将要跳转的目标id，
            int id = item.getItemId();
            Class<?> targetClass = null;
            
            if (id == R.id.nav_home) { //根据获取的id，判断要跳转到哪个页面
                targetClass = BtnActivity.class; // 跳转目标为BtnActivity
            } else if (id == R.id.nav_play) {
                targetClass = JumpTo.class;
            } else if (id == R.id.nav_wallpaper) {
                targetClass = SaveDataAndFunc.class;
            }
            
            if (targetClass != null && !targetClass.equals(context.getClass())) { // 防止重复跳转
                navigateTo(context, targetClass);  // 传入 当前页面的活动的类，跳转目标类
            }
            return true;
        });
    }

    private static void navigateTo(Context context, Class<?> cls) {
        
        // 打印当前任务栈信息
//        if (context instanceof Activity) {
//            try {
//                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(10); // 获取最近10个任务
//                for (ActivityManager.RunningTaskInfo task : tasks) {
//                    Log.d("TaskStack", "Task ID: " + task.id);
//                    Log.d("TaskStack", "Top Activity: " + task.topActivity.getClassName());
//                    Log.d("TaskStack", "Base Activity: " + task.baseActivity.getClassName());
//                    Log.d("TaskStack", "Num Activities: " + task.numActivities);
//                    Log.d("TaskStack", "----------------------");
//                }
//            } catch (SecurityException e) {
//                Log.e("TaskStack", "需要GET_TASKS权限", e);
//            }
//        }
    
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP); // 清除栈顶所有活动并将新活动置于栈顶
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 启动新任务
        }
        context.startActivity(intent); // 启动目标活动
        
        if (context instanceof Activity) { // 检查是否是 Activity 实例
            ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 添加淡入淡出动画
        }
    }
}