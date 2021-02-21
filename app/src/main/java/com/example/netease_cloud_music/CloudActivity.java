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
import android.view.View;
import android.widget.Toast;

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

public class CloudActivity extends AppCompatActivity {
   private HashMap<String,String>map=new HashMap<>();
   private List<Item_music>list=new ArrayList<>();
   private PostNetRequest request=new PostNetRequest();
   private Mhandler mhandler=new Mhandler();
   private MusicAdapter adapter;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
       CoverActivity.activityList.add(this);
       View decorview =getWindow().getDecorView();
       decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
               |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
       getWindow().setStatusBarColor(Color.TRANSPARENT);

       SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
       String cookie=pref.getString("cookie","");
        map.put("cookie",cookie);
        request.sendPostRequest("http://sandyz.ink:3000/user/cloud",map,mhandler,1);

       RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycleview_cloud);
       LinearLayoutManager layoutManager=new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layoutManager);
       adapter=new MusicAdapter(this,list);
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
        Intent intent=new Intent(CloudActivity.this,Music_play_Activity.class);
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
                    JSONObject jsonObject=new JSONObject(respondata);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    if(jsonArray.length()==0){
                        Toast.makeText(CloudActivity.this,"暂时没有歌曲哦，快去上传吧",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        for (int i = 0; i <jsonArray.length() ; i++) {
                            JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                            String music_name=jsonObjectI.getString("songName");
                            String music_singer=jsonObjectI.getString("artist");
                            int music_count=i+1;
                            long music_id=jsonObjectI.getLong("songId");
                            JSONObject jsonObjectsong=jsonObjectI.getJSONObject("simpleSong");
                            JSONObject jsonObjectal=jsonObjectsong.getJSONObject("al");
                            String music_al=jsonObjectal.getString("name");
                            String music_img=jsonObjectal.getString("picUrl");
                            Item_music music=new Item_music(music_name,music_singer,music_al,music_count,music_id,music_img,2);
                            list.add(music);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}