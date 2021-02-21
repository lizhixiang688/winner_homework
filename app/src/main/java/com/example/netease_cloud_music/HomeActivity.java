package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import fragment.Home_fragment;
import fragment.User_fragment;
import tool.TablayoutAdapter;

public class HomeActivity extends AppCompatActivity {
     private TabLayout tabLayout;
     private ViewPager viewPager;
     private List<Fragment> fragmentList =new ArrayList<>();
     private List<String> fragmenttitle = new ArrayList<>();
     private TablayoutAdapter tablayoutAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        fragmenttitle.add("发现");
        fragmenttitle.add("我的");
        fragmentList.add(new Home_fragment());
        fragmentList.add(new User_fragment());
        tablayoutAdapter=new TablayoutAdapter(getSupportFragmentManager(),TablayoutAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                                              fragmentList,fragmenttitle);
        viewPager.setAdapter(tablayoutAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    class MHanlder extends Handler{

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

}