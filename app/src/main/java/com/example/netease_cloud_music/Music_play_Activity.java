package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import Item.Item_music;
import service.PlayService;
import tool.GlideCircleTransform;
import NetRequest.MyJson;
import NetRequest.PostNetRequest_String_Long;

public class Music_play_Activity extends AppCompatActivity {
    private View need;
     private List<Item_music> list;
     private int position;
     private ImageView play;
     private TextView currenttime,totaltime;
     private SeekBar seekBar;
     private long music_id,playlist_id;
     private String music_img;
     private String music_url;
     private ImageView imageView,imgageview_big;
     private HashMap<String,Long>map=new HashMap<>();
     private PostNetRequest_String_Long mypost=new PostNetRequest_String_Long();
     private MHandler mHandler=new MHandler();
     private PlayService.MyBinder myBinder;
     private SimpleDateFormat format=new SimpleDateFormat("mm:ss");
     private Handler handler=new Handler();
     private ObjectAnimator animator;
     private ObjectAnimator animator1;
     private Service mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play_);
        CoverActivity.activityList.add(this);

        View decorview =getWindow().getDecorView();                  //实现隐藏导航栏和状态栏
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        link();
        music_id=getIntent().getLongExtra("music_id",0);
        music_img=getIntent().getStringExtra("music_img");
        list=(List<Item_music>) getIntent().getSerializableExtra("music_list");   //传了一个list进来，里面有所有的歌曲id
        position=getIntent().getIntExtra("position",0);               //传入当前的音乐是第几个
        playlist_id=getIntent() .getLongExtra("playlist_id",0);
        Glide.with(this).load(music_img).transform(new GlideCircleTransform(this)).crossFade().into(imageView);




        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);                                                  //设置动画

        map.put("id",music_id);
        mypost.sendPostRequest("http://sandyz.ink:3000/song/url",map,mHandler,1);

    }

    public void link(){
        imageView=(ImageView)findViewById(R.id.img_music);
        seekBar=(SeekBar)findViewById(R.id.seek_bar);
        play=(ImageView)findViewById(R.id.play);
        currenttime=(TextView)findViewById(R.id.music_current_time);
        totaltime=(TextView)findViewById(R.id.music_total_time);
    }


    private ServiceConnection serviceconnection=new ServiceConnection() {     //这是绑定的重写方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder=(PlayService.MyBinder)service;
            mservice=myBinder.service();
            seekBar.setMax(myBinder.getProgress());
            totaltime.setText(format.format(myBinder.getProgress()));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser==true){
                        myBinder.seekToPositon(progress);
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            myBinder.setplaylist(playlist_id);
            myBinder.setPosition(position);
            myBinder.preparemusic(music_url);
            myBinder.setList(list);
            handler.post(runnable);                              //启动接口
            myBinder.Play();
            play.setImageResource(R.drawable.icon_pause_wr);
            animator.start();

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private Runnable runnable=new Runnable() {                  //实现进度条功能的接口
        @Override
        public void run() {
            Item_music itemMusic=list.get(myBinder.getPosition());
            Glide.with(Music_play_Activity.this).load(itemMusic.getMusic_url()).transform(new GlideCircleTransform(Music_play_Activity.this)).crossFade().into(imageView);
            totaltime.setText(format.format(myBinder.getProgress()));
            currenttime.setText(format.format(myBinder.getPlayPosition()));
            seekBar.setMax(myBinder.getProgress());
            seekBar.setProgress(myBinder.getPlayPosition());
            handler.postDelayed(runnable,200);
        }
    };


    protected void onDestroy() {                             //重写的Ondestory方法
        super.onDestroy();
        handler.removeCallbacks(runnable);
        unbindService(serviceconnection);
    }

    public void play_pause(View view) {
        this.need=view;
        if(!myBinder.isplayling()){
            myBinder.Play();
            play.setImageResource(R.drawable.icon_pause_wr);
            animator.resume();

        }
        else {
            myBinder.Pause();
            play.setImageResource(R.drawable.icon_play_wr);
            animator.pause();
        }
    }

    public void next(View view) {
      HashMap<String,Long> map_pre=new HashMap<>();
      if(position==list.size()-1){
          Item_music item_music=list.get(0);
          position=0;
          myBinder.setPosition(0);
          map_pre.put("id",item_music.getMusic_id());
          Glide.with(this).load(item_music.getMusic_url()).transform(new GlideCircleTransform(this)).crossFade().into(imageView);
          handler.removeCallbacks(runnable);
          mypost.sendPostRequest("http://sandyz.ink:3000/song/url",map_pre,mHandler,2);
          play.setImageResource(R.drawable.icon_pause_wr);
          animator.start();
      }
      else {
          Item_music item_music=list.get(++position);
          myBinder.setPosition(position);
          map_pre.put("id",item_music.getMusic_id());
          Glide.with(this).load(item_music.getMusic_url()).transform(new GlideCircleTransform(this)).crossFade().into(imageView);
          handler.removeCallbacks(runnable);
          mypost.sendPostRequest("http://sandyz.ink:3000/song/url",map_pre,mHandler,2);
          play.setImageResource(R.drawable.icon_pause_wr);
          animator.start();
      }

    }

    public void previous(View view) {
        HashMap<String,Long> map_pre=new HashMap<>();
        if(position==0){
            Item_music item_music=list.get(list.size()-1);
            position=list.size()-1;
            myBinder.setPosition(position);
            map_pre.put("id",item_music.getMusic_id());
            Glide.with(this).load(item_music.getMusic_url()).transform(new GlideCircleTransform(this)).crossFade().into(imageView);
            handler.removeCallbacks(runnable);
            mypost.sendPostRequest("http://sandyz.ink:3000/song/url",map_pre,mHandler,2);
            play.setImageResource(R.drawable.icon_pause_wr);
            animator.start();
        }
        else {
            Item_music item_music=list.get(--position);
            myBinder.setPosition(position);
            map_pre.put("id",item_music.getMusic_id());
            Glide.with(this).load(item_music.getMusic_url()).transform(new GlideCircleTransform(this)).crossFade().into(imageView);
            handler.removeCallbacks(runnable);
            mypost.sendPostRequest("http://sandyz.ink:3000/song/url",map_pre,mHandler,2);
            play.setImageResource(R.drawable.icon_pause_wr);
            animator.start();
        }
    }
    public void uichange(){
        seekBar.setMax(myBinder.getProgress());
        totaltime.setText(format.format(myBinder.getProgress()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true){
                    myBinder.seekToPositon(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        handler.post(runnable);
    }


    private class MHandler extends Handler{
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
                    JSONObject jsonObjectdata=jsonArray.getJSONObject(0);
                    music_url=jsonObjectdata.getString("url");

                    Intent intent=new Intent(Music_play_Activity.this,PlayService.class);
                    intent.putExtra("music_url",music_url);
                    bindService(intent,serviceconnection,Music_play_Activity.BIND_AUTO_CREATE);     //绑定服务
                    startService(intent);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else if(flag==2){
                try {
                    JSONObject jsonObject=new JSONObject(respondata);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    JSONObject jsonObjectdata=jsonArray.getJSONObject(0);
                    music_url=jsonObjectdata.getString("url");
                    myBinder.change(music_url);
                    uichange();           //重新初始化之后要重新设置ui，比如，进度条
                    myBinder.Play();      //初始化之后直接开始播放
            }catch (JSONException e){
                    e.printStackTrace();
                }
        }
    }
}
}