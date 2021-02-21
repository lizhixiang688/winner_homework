package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Item.Item_follow;
import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import tool.FollowAdapter;

public class FollowActivity extends AppCompatActivity {
    private List<Item_follow>list=new ArrayList<>();
    private HashMap<String,String>map=new HashMap<>();
    private PostNetRequest request=new PostNetRequest();
    private Mhandler mhandler=new Mhandler();
    private FollowAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        CoverActivity.activityList.add(this);
        recyclerView=(RecyclerView)findViewById(R.id.recycle_follow);

        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
        Long userid=pref.getLong("userid",0);
        map.put("uid",""+userid);
        request.sendPostRequest("http://sandyz.ink:3000/user/follows",map,mhandler,1);

        adapter=new FollowAdapter(this,list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    public void back_touser(View view) {
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,R.anim.close);
    }
    private class Mhandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MyJson myJson=(MyJson)msg.obj;
            String respondata=myJson.respondata;
            int flag=myJson.flag;
            if(flag==1){
                try {
                    JSONObject jsonObject=new JSONObject(respondata);
                    JSONArray jsonArray=jsonObject.getJSONArray("follow");
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                        String name=jsonObjectI.getString("nickname");
                        String avatar=jsonObjectI.getString("avatarUrl");
                        Item_follow item_follow=new Item_follow(name,avatar);
                        list.add(item_follow);
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}