package service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Item.Item_music;
import NetRequest.MyJson;
import NetRequest.PostNetRequest_String_Long;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {
    public MediaPlayer mediaPlayer=new MediaPlayer();
    private String musciurl;
    private long playlist_id,currplaylist_id;
    public MyBinder myBinder=new MyBinder();    //内部类
    private List<Item_music>mlist;
    private int mposition,currpositin=-1;
    private HashMap<String,Long>map=new HashMap<>();
    private PostNetRequest_String_Long request=new PostNetRequest_String_Long();
    private Mhandler mhandler=new Mhandler();

    public PlayService() {

    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mposition==mlist.size()-1){
            Item_music itemMusic = mlist.get(0);
            mposition=0;
            long id =itemMusic.getMusic_id();
            map.put("id",id);
            request.sendPostRequest("http://sandyz.ink:3000/song/url",map,mhandler,1);
        }
        else{
            Item_music itemMusic=mlist.get(++mposition);
           map.put("id",itemMusic.getMusic_id());
            request.sendPostRequest("http://sandyz.ink:3000/song/url",map,mhandler,1);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        musciurl=intent.getStringExtra("music_url");
        initmediaplayer(musciurl);

        return myBinder;
    }


        @Override
        public void onCreate () {
            super.onCreate();

        }

        @Override
        public int onStartCommand (Intent intent,int flags, int startId){


            return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public void onDestroy () {

        }
        public void initmediaplayer(String url){
        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this::onCompletion);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

        public class MyBinder extends Binder {              //内部类
           public PlayService service() {
                return PlayService.this;
            }


            public void Play() {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
              currpositin=mposition;
                currplaylist_id=playlist_id;
            }


            public void Pause(){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }


            public void change(String url){
                mediaPlayer.stop();
                mediaPlayer.reset();
                initmediaplayer(url);

            }
            public void setList(List<Item_music> list){
               mlist=list;
            }
            public void setPosition(int position){
               mposition=position;
            }
            public int getProgress() {
                return mediaPlayer.getDuration();
            }
            public void setplaylist(long id){
               playlist_id =id;
            }
            public boolean isplayling(){
                return mediaPlayer.isPlaying();
            }
            public void preparemusic(String url){
               if(mposition!=currpositin||currplaylist_id!=playlist_id){
                   if(mediaPlayer!=null){
                       mediaPlayer.stop();
                       mediaPlayer.reset();
                   }
                   initmediaplayer(url);
               }

            }

            public int getPlayPosition() {
                return mediaPlayer.getCurrentPosition();
            }
            public int getPosition(){
               return mposition;
            }
            public void seekToPositon(int msec) {
                mediaPlayer.seekTo(msec);
            }


            public void closeMedia() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }

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
                        JSONObject jsonObjectdata=jsonArray.getJSONObject(0);
                         String music_url=jsonObjectdata.getString("url");
                         myBinder.change(music_url);
                         myBinder.Play();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        }

}
