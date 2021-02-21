package com.example.netease_cloud_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import NetRequest.PostNetRequest_String_Long;
import tool.TablayoutAdapter;

public class RegisterActivity extends AppCompatActivity {
    private EditText phone,ver,password,nickname;
    private Counttime time;
    private Button button,button1;
    private Mhandler mhandler=new Mhandler();
    private HashMap<String,Long>map=new HashMap<>();
    private HashMap<String,String>map1=new HashMap<>();
    private PostNetRequest_String_Long myrequest=new PostNetRequest_String_Long();
    private PostNetRequest request=new PostNetRequest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CoverActivity.activityList.add(this);

        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        link();
        button=(Button)findViewById(R.id.send_verification1);
        time=new Counttime(60000,1000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    time.start();
                    Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                    String number=phone.getText().toString();
                    map.put("phone",Long.parseLong(number));
                    myrequest.sendPostRequest("http://sandyz.ink:3000/captcha/sent",map,mhandler,1);
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number=phone.getText().toString();
                String verication=ver.getText().toString();
                String mpassword=password.getText().toString();
                String name=nickname.getText().toString();
                map1.put("phone",phone_number);
                map1.put("password",mpassword);
                map1.put("captcha",verication);
                map1.put("nickname",name);
                request.sendPostRequest("http://sandyz.ink:3000/register/cellphone",map1,mhandler,2);
            }
        });
    }
    private void link(){
        phone=(EditText)findViewById(R.id.edittext_phone1);
        ver=(EditText)findViewById(R.id.edit_verfication1);
        password=(EditText)findViewById(R.id.edit_password1);
        nickname=(EditText)findViewById(R.id.edit_nickname1);
        button1=(Button)findViewById(R.id.to_register);
    }

    public void to_return(View view) {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }



    class Counttime extends CountDownTimer {
        public Counttime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            button.setClickable(false);
            button.setText(l/1000+"秒后重新获取");
        }

        @Override
        public void onFinish() {
            button.setClickable(true);
            button.setText("获取验证码");
        }
    }
    private class Mhandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MyJson myJson=(MyJson)msg.obj;
            String respondata=myJson.respondata;
            int flag=myJson.flag;
           if(flag==2){
               try {
                   JSONObject jsonObject=new JSONObject(respondata);
                   int code=jsonObject.getInt("code");
                   if(code!=200){
                       Toast.makeText(RegisterActivity.this,"该昵称已被占用",Toast.LENGTH_SHORT).show();
                   }
                   else{
                       Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                       Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                       startActivity(intent);
                   }
               }catch (JSONException e){
                   e.printStackTrace();
               }
           }
        }
    }
}