package com.example.netease_cloud_music;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;

import java.util.LinkedList;
import java.util.List;

public class CoverActivity extends AppCompatActivity {
    public static List<Activity> activityList = new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_cover);
        CoverActivity.activityList.add(this);     //实现一键关闭app功能

        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setExitTransition(fade);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
               Long userid=pref.getLong("userid",0);
               String cookie=pref.getString("cookie","");
               if(userid==0) {
                   Intent intent = new Intent(CoverActivity.this, LoginActivity.class);
                   startActivity(intent);
               }
               else {
                   Intent intent=new Intent(CoverActivity.this,HomeActivity.class);
                   intent.putExtra("userid",userid);
                   intent.putExtra("cookie",cookie);
                   startActivity(intent);
               }
               finish();
           }

       },2000);
}
    public static void exit(){
        for(Activity act:activityList){
            act.finish();
        }
        System.exit(0);
    }

}