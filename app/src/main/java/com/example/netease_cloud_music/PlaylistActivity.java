package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Item.Item_music;
import NetRequest.PostNetRequest;
import tool.GlideRoundTransform;
import tool.MusicAdapter;
import NetRequest.MyJson;
import NetRequest.PostNetRequest_String_Long;

public class PlaylistActivity extends AppCompatActivity {
    private long playlist_id,music_id;
    private String music_name,music_singer,music_album,music_url;
    private String playlist_name,playlist_cover,cookie;
    private int music_count;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private HashMap<String,String>map=new HashMap<>();
    private List<Item_music>list=new ArrayList<>();
    private PostNetRequest myquest=new PostNetRequest();
    private MHandler mHanlder=new MHandler();
    private MusicAdapter adapter;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        CoverActivity.activityList.add(this);

        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        imageView=(ImageView)findViewById(R.id.cover_playlist);
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsingToolbarLayout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MusicAdapter(this,list);
        recyclerView.setAdapter(adapter);

        SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
        cookie=pref.getString("cookie","");
        playlist_id=getIntent().getLongExtra("playlist_id",0);
        playlist_name=getIntent().getStringExtra("playlist_name");
        playlist_cover=getIntent().getStringExtra("playlist_cover");
        Glide.with(this).load(playlist_cover).transform(new GlideRoundTransform(PlaylistActivity.this,8)).into(imageView);
        collapsingToolbarLayout.setTitle(playlist_name);
        map.put("id",""+playlist_id);
        map.put("cookie",cookie);
        myquest.sendPostRequest("http://sandyz.ink:3000/playlist/detail",map,mHanlder,1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MHandler extends Handler{

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        MyJson myJson=(MyJson)msg.obj;
        String respondata=myJson.respondata;
        int flag=myJson.flag;
        if(flag==1){
            try {
                JSONObject jsonObject=new JSONObject(respondata);
                JSONObject jsonObjectplaylist=jsonObject.getJSONObject("playlist");
                JSONArray jsonArray=jsonObjectplaylist.getJSONArray("tracks");
                for (int i = 0; i <jsonArray.length() ; i++) {
                  JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                  music_count=i+1;
                  music_name=jsonObjectI.getString("name");
                  music_id=jsonObjectI.getLong("id");
                  JSONArray jsonArrayar=jsonObjectI.getJSONArray("ar");
                    JSONObject jsonObject1=jsonArrayar.getJSONObject(0);
                    music_singer=jsonObject1.getString("name");
                    JSONObject jsonObjectal=jsonObjectI.getJSONObject("al");
                    music_album=jsonObjectal.getString("name");
                    music_url=jsonObjectal.getString("picUrl");
                    Item_music item=new Item_music(music_name,music_singer,music_album,music_count,music_id,music_url,playlist_id);
                    list.add(item);
                    adapter.notifyDataSetChanged();
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}
    
}