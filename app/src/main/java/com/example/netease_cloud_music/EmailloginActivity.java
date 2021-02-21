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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import NetRequest.MyJson;
import NetRequest.PostNetRequest;

public class EmailloginActivity extends AppCompatActivity {
    private EditText edit_email,edit_password;
    private ProgressBar progressBar;
    private Button button;
    private HashMap<String,String>map=new HashMap<>();
    private PostNetRequest request=new PostNetRequest();
    private MHanlder mHanlder=new MHanlder();
    private int code;
    private String cookie;
    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emaillogin);
        CoverActivity.activityList.add(this);
        View decorview =getWindow().getDecorView();                  //实现隐藏导航栏和状态栏
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        link();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit_email.getText().toString();
                String password=edit_password.getText().toString();
                map.put("email",email);
                map.put("password",password);
                request.sendPostRequest("http://sandyz.ink:3000/login",map,mHanlder,1);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    private void link(){
        edit_email=(EditText)findViewById(R.id.edittext_login_email);
        edit_password=(EditText)findViewById(R.id.edittext_login_password_email);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_1);
        button=(Button)findViewById(R.id.button_email_login_1);
    }
    private class MHanlder extends Handler {
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
                    Toast.makeText(EmailloginActivity.this, "账号不存在或账号错误", Toast.LENGTH_SHORT).show();
                } else if (code == 502) {
                    Toast.makeText(EmailloginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(EmailloginActivity.this, HomeActivity.class);
                    intent.putExtra("userid",userid);
                    intent.putExtra("cookie",cookie);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}