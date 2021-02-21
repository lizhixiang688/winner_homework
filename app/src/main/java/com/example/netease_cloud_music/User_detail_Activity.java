package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import tool.GlideCircleTransform;

public class User_detail_Activity extends AppCompatActivity {
    private String cookie;
    private Long userid;
    private ImageView user_icon;
    private TextView user_name,detail_1,detail_2,detail_3;
    private TextView age,fan,focus,level,tolisten,tologin,totallisten,totalplaylist;
    private Mhandler mhandler=new Mhandler();
    private PostNetRequest mrequest=new PostNetRequest();
    private HashMap<String,String>map1=new HashMap<>();
    private HashMap<String,String>map2=new HashMap<>();
    private SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        cookie=getIntent().getStringExtra("cookie");
        userid=getIntent().getLongExtra("userid",0);
        link();
        detail_1.getPaint().setFakeBoldText(true);
        detail_2.getPaint().setFakeBoldText(true);
        detail_3.getPaint().setFakeBoldText(true);
        map1.put("uid",Long.toString(userid));
        map2.put("cookie",cookie);
        mrequest.sendPostRequest("http://sandyz.ink:3000/user/detail",map1,mhandler,1);
        mrequest.sendPostRequest("http://sandyz.ink:3000/user/level",map2,mhandler,2);
    }
    private void link(){
        user_icon=(ImageView)findViewById(R.id.avatar);
        user_name=(TextView)findViewById(R.id.name);
        detail_1=(TextView)findViewById(R.id.text_details_1);
        detail_2=(TextView)findViewById(R.id.text_details_2);
        detail_3=(TextView)findViewById(R.id.text_details_3);
        age=(TextView)findViewById(R.id.age);
        fan=(TextView)findViewById(R.id.fan);
        focus=(TextView)findViewById(R.id.focus);
        level=(TextView)findViewById(R.id.level);
        tolisten=(TextView)findViewById(R.id.tolisten);
        tologin=(TextView)findViewById(R.id.tologin);
        totallisten=(TextView)findViewById(R.id.totallisten);
        totalplaylist=(TextView)findViewById(R.id.totalplaylist);
    }

    public void login_out(View view) {
        AlertDialog builder = new AlertDialog.Builder(User_detail_Activity.this)
                .setTitle("确定退出当前账号吗？")
                .setCancelable(false)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        CoverActivity.exit();
                    }
                })
                .setNegativeButton("取消", null)
                .show();

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
        builder.getWindow().setBackgroundDrawableResource(android.R.color.white);
        try {
            //通过反射获取mAlert对象
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(builder);
            //获取mTitleView并设置大小颜色
            Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
            mTitle.setAccessible(true);
            TextView mTitleView = (TextView) mTitle.get(mAlertController);
            mTitleView.setTextSize(15);
            mTitleView.setTextColor(Color.BLACK);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
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
                  int totalsong=jsonObject.getInt("listenSongs");
                  JSONObject jsonObjectdata=jsonObject.getJSONObject("profile");
                  String avatarurl=jsonObjectdata.getString("avatarUrl");
                  String name=jsonObjectdata.getString("nickname");
                  int followeds=jsonObjectdata.getInt("followeds");
                  int follows=jsonObjectdata.getInt("follows");
                  int playlistcount=jsonObjectdata.getInt("playlistCount");
                  int createDays=jsonObject.getInt("createDays");
                  long createtime=jsonObject.getLong("createTime");
                  Glide.with(User_detail_Activity.this).load(avatarurl).transform(new GlideCircleTransform(User_detail_Activity.this)).into(user_icon);
                  user_name.setText(name);
                  String time=format.format(createtime);
                  age.setText(createDays+"天"+" "+"("+time+"注册"+")");
                  fan.setText(""+followeds);
                  focus.setText(""+follows);
                  totallisten.setText(""+totalsong+"首");
                  totalplaylist.setText(""+playlistcount+"个");
              }catch (JSONException e){
                  e.printStackTrace();
              }
            }
            else if(flag==2){
                try {
                    JSONObject jsonObject=new JSONObject(respondata);
                    JSONObject jsonObjectdata=jsonObject.getJSONObject("data");
                    int lv=jsonObjectdata.getInt("level");
                    int nextlisten=(jsonObjectdata.getInt("nextPlayCount")-jsonObjectdata.getInt("nowPlayCount"));
                    int nextlogin=(jsonObjectdata.getInt("nextLoginCount")-jsonObjectdata.getInt("nowLoginCount"));
                    level.setText("Lv."+lv);
                    tolisten.setText("还需"+nextlisten+"首");
                    tologin.setText("还需"+nextlogin+"天");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }

    }
}