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

public class VerificationloginActivity extends AppCompatActivity {
    private EditText phone_number,verification,new_password;
    private Button button;
    private  Counttime time;
    private HashMap<String,Long> map=new HashMap<>();
    private HashMap<String,String> map1=new HashMap<>();
    private PostNetRequest_String_Long myrequest=new PostNetRequest_String_Long();
    private PostNetRequest request=new PostNetRequest();
    private Mhandler mhandler=new Mhandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationlogin);
        CoverActivity.activityList.add(this);

        View decorview =getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        phone_number=(EditText)findViewById(R.id.edittext_phone);
        verification=(EditText)findViewById(R.id.edit_verfication);
        new_password=(EditText)findViewById(R.id.edit_password);
        button=(Button)findViewById(R.id.send_verification);
        time=new Counttime(60000,1000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone_number.getText().toString().isEmpty()){
                    Toast.makeText(VerificationloginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    time.start();
                    Toast.makeText(VerificationloginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    String phonenumber = phone_number.getText().toString();
                    map.put("phone", Long.parseLong(phonenumber));
                    myrequest.sendPostRequest("http://sandyz.ink:3000/captcha/sent",map,mhandler,1);
                }
            }
        });
    }

    public void to_return(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void change_password(View view) {
        String phone=phone_number.getText().toString();
      String ver=verification.getText().toString();
      String password=new_password.getText().toString();
      map1.put("phone",phone);
      map1.put("password",password);
      map1.put("captcha",ver);
      request.sendPostRequest("http://sandyz.ink:3000/register/cellphone",map1,mhandler,2);
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
                    JSONObject jsonObject = new JSONObject(respondata);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        Toast.makeText(VerificationloginActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(VerificationloginActivity.this,LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerificationloginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}