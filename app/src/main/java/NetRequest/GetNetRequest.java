package NetRequest;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNetRequest {

    public void sendGetRequest(String murl, Handler handler,int flag){
        new Thread(
                ()->{
                    try{
                        String m_url=murl+"?timestamp="+System.currentTimeMillis();
                        URL url=new URL(m_url);
                        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.connect();
                        InputStream in=connection.getInputStream();
                        String respondata =StreamToString(in);
                        MyJson myJson=new MyJson(respondata,flag);
                        Message message=new Message();
                        message.obj=myJson;
                        handler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
        ).start();

    }

    public String StreamToString(InputStream in){
        StringBuilder sb=new StringBuilder();
        String oneline;
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        try{
            while((oneline=reader.readLine())!=null){
                sb.append(oneline).append('\n');
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
