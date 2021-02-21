package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Item.Item_music;
import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import tool.MusicAdapter;

public class Recent_playActivity extends AppCompatActivity {
    private  HashMap<String,String>map=new HashMap<>();
    private  PostNetRequest request=new PostNetRequest();
    private long id;
    private String cookie;
    private Mhandler mhandler=new Mhandler();
    private List<Item_music>list=new ArrayList<>();
    private MusicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_play);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
        id=pref.getLong("userid",0);
        map.put("uid",""+id);
        map.put("type","1");
        request.sendPostRequest("http://sandyz.ink:3000/user/record",map,mhandler,1);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MusicAdapter( this,list);
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

    public void play_all(View view) {
        Intent intent=new Intent(Recent_playActivity.this,Music_play_Activity.class);
        Item_music itemMusic=list.get(0);
        intent.putExtra("music_list",(Serializable)list);
        intent.putExtra("position",0);
        intent.putExtra("music_id",itemMusic.getMusic_id());
        intent.putExtra("music_img",itemMusic.getMusic_url());
        intent.putExtra("playlist_id",itemMusic.getPlaylist_id());
        startActivity(intent);
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
                    Log.d("lzx", respondata);
                    JSONObject jsonObject=new JSONObject(respondata);
                    JSONArray jsonArray=jsonObject.getJSONArray("weekData");
                    Log.d("lzx", "112");
                    Log.d("lzx", ""+jsonArray.length());
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        Log.d("lzx", ""+i);
                        JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                        JSONObject jsonObjectsong=jsonObjectI.getJSONObject("song");
                        int musiccout=i+1;
                        String musicname=jsonObjectsong.getString("name");
                        long musicid=jsonObjectsong.getLong("id");
                        JSONArray jsonArrayar=jsonObjectsong.getJSONArray("ar");
                        JSONObject jsonObjectar1=jsonArrayar.getJSONObject(0);
                        String musicsinger=jsonObjectar1.getString("name");
                        JSONObject jsonObjectal=jsonObjectsong.getJSONObject("al");
                        String musical=jsonObjectal.getString("name");
                        String musicurl=jsonObjectal.getString("picUrl");

                        Item_music itemMusic=new Item_music(musicname,musicsinger,musical,musiccout,musicid,musicurl,1);
                        list.add(itemMusic);
                        adapter.notifyDataSetChanged();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}