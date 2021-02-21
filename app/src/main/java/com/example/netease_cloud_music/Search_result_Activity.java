package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import Item.Item_music;
import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import tool.SearchAdapter;

public class Search_result_Activity extends AppCompatActivity {
    private long musicid;
    private String music_name,music_singer,music_album,music_url;
    private PostNetRequest request=new PostNetRequest();
    private HashMap<String,String> map=new HashMap<>();
    private Mhandler mhandler=new Mhandler();
    private String to_search;
    private SearchAdapter adapter;
    private List<Item_music> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        to_search=getIntent().getStringExtra("to_search");
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycle_search);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SearchAdapter(this,list);
        recyclerView.setAdapter(adapter);
        map.put("keywords",to_search);
        request.sendPostRequest("http://sandyz.ink:3000/search",map,mhandler,1);
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
                   JSONObject jsonObjectresult=jsonObject.getJSONObject("result");
                   JSONArray jsonArray=jsonObjectresult.getJSONArray("songs");
                    for (int i = 0; i <15 ; i++) {
                        JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                        musicid=jsonObjectI.getLong("id");
                        music_name=jsonObjectI.getString("name");
                        JSONArray jsonArrayar=jsonObjectI.getJSONArray("artists");
                        JSONObject jsonObjectar1=jsonArrayar.getJSONObject(0);
                        music_singer=jsonObjectar1.getString("name");
                        JSONObject jsonObjectal=jsonObjectI.getJSONObject("album");
                        music_album=jsonObjectal.getString("name");
                        music_url=jsonObjectar1.getString("img1v1Url");
                        Item_music music=new Item_music(music_name,music_singer,music_album,i,musicid,music_url,0);
                        list.add(music);
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}