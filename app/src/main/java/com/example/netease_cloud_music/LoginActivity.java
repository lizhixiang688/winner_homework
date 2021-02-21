package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import NetRequest.MyJson;
import NetRequest.PostNetRequest;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_ID;
    private EditText editText_password;
    private ProgressBar progressBar;
    private String ID,password;
    private long userid;
    private HashMap<String,String> map =new HashMap<>();
    private PostNetRequest myrequest=new PostNetRequest();          //实例化工具类
    private MHanlder mHanlder=new MHanlder();
    private int code;
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();                  //实现隐藏导航栏和状态栏
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        link();
    }

    private void link(){
        editText_ID=(EditText)findViewById(R.id.edittext_login_ID);
        editText_password=(EditText)findViewById(R.id.edittext_login_password);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
    }

    public void login(View view) {
        ID=editText_ID.getText().toString();
        password=editText_password.getText().toString();
        map.put("phone",ID);
        map.put("password",password);
        myrequest.sendPostRequest("http://sandyz.ink:3000/login/cellphone",map,mHanlder,1);
        progressBar.setVisibility(View.VISIBLE);
    }

       //下面是点击事件
    public void login_email(View view) {
        Intent intent=new Intent(this,EmailloginActivity.class);
        startActivity(intent);
    }
    public void register(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void verification(View view) {
        Intent intent=new Intent(this,VerificationloginActivity.class);
        startActivity(intent);
    }




    private class MHanlder extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MyJson myJson=(MyJson)msg.obj;
            String responsedata=myJson.respondata;
            int flag=myJson.flag;
            if(flag==1) {
                try {
                    JSONObject jsonObject = new JSONObject(responsedata);
                    code = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                if (code == 501 || code == 400) {
                    Toast.makeText(LoginActivity.this, "账号不存在或账号错误", Toast.LENGTH_SHORT).show();
                } else if (code == 502) {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    try{
                        JSONObject jsonObject=new JSONObject(responsedata);
                        JSONObject jsonObjectdata=jsonObject.getJSONObject("account");
                        userid=jsonObjectdata.getLong("id");
                        cookie=jsonObject.getString("cookie");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
                    editor.putLong("userid",userid);
                    editor.putString("cookie",cookie);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("cookie",cookie);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}