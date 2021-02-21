package fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.netease_cloud_music.CloudActivity;
import com.example.netease_cloud_music.FollowActivity;
import com.example.netease_cloud_music.R;
import com.example.netease_cloud_music.Recent_playActivity;
import com.example.netease_cloud_music.User_detail_Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import NetRequest.GetNetRequest;
import tool.GlideCircleTransform;
import NetRequest.MyJson;
import NetRequest.PostNetRequest;
import Item.Item_playlist;
import tool.RecycleAdapter;


public class User_fragment extends Fragment {
    private ImageView user_icon,img_detail,img_usedplay,img_cloud,img_friend;
    private TextView user_name,user_level;
    private int level,music_count;
    private long id,playlist_id;
    private String name,icon_url,playlist_url,playlist_name,cookie;
    private HashMap<String,String>map1=new HashMap<>();
    private HashMap<String,String>map2=new HashMap<>();
    private PostNetRequest myrequest=new PostNetRequest();
    private MHandler mHandler=new MHandler();
    private List<Item_playlist> item_list=new ArrayList<>();
    private RecycleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle=getActivity().getIntent().getExtras();
        id=bundle.getLong("userid");
        cookie=bundle.getString("cookie");
        map1.put("uid",Long.toString(id));
        map2.put("cookie",cookie);
        myrequest.sendPostRequest("http://sandyz.ink:3000/user/detail",map1,mHandler,1);
        myrequest.sendPostRequest("http://sandyz.ink:3000/user/level",map2,mHandler,2);
        myrequest.sendPostRequest("http://sandyz.ink:3000/user/playlist",map1,mHandler,3);
        return inflater.inflate(R.layout.fragment_user_fragment, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user_name=(TextView)getActivity().findViewById(R.id.user_name);
        user_icon=(ImageView)getActivity().findViewById(R.id.user_icon);
        user_level=(TextView)getActivity().findViewById(R.id.user_level);
        img_usedplay=(ImageView)getActivity().findViewById(R.id.img_3);
        img_friend=(ImageView)getActivity().findViewById(R.id.img_1);
        img_cloud=(ImageView)getActivity().findViewById(R.id.img_2);
        img_detail=(ImageView)getActivity().findViewById(R.id.img_detail);
        img_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), User_detail_Activity.class);
                intent.putExtra("userid",id);
                intent.putExtra("cookie",cookie);
                getActivity().startActivity(intent);
            }
        });
        img_usedplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Recent_playActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.open,R.anim.close);
            }
        });
        img_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FollowActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.open,R.anim.close);
            }
        });
        img_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CloudActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.open,R.anim.close);
            }
        });
        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.recycle_laylist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecycleAdapter(getActivity(),item_list);
        recyclerView.setAdapter(adapter);
    }

    private class MHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MyJson myJson=(MyJson)msg.obj;
            String responsedata=myJson.respondata;
            int flag=myJson.flag;
            if(flag==1){
                try {
                    JSONObject jsonObject=new JSONObject(responsedata);
                    JSONObject jsonObjectprofile=jsonObject.getJSONObject("profile");
                    name=jsonObjectprofile.getString("nickname");
                    icon_url=jsonObjectprofile.getString("avatarUrl");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                 user_name.setText(name);
                 Glide.with(getActivity()).load(icon_url).transform(new GlideCircleTransform(getActivity())).into(user_icon);
            }
            else if(flag==2){
                try {
                    JSONObject jsonObject=new JSONObject(responsedata);
                    JSONObject jsonObjectdata=jsonObject.getJSONObject("data");
                    level=jsonObjectdata.getInt("level");
                    user_level.setText("Lv."+level);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            else if(flag==3){
                try {
                    JSONObject jsonObject=new JSONObject(responsedata);
                    JSONArray jsonArray=jsonObject.getJSONArray("playlist");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObjectI=jsonArray.getJSONObject(i);
                        playlist_name=jsonObjectI.getString("name");
                        playlist_url=jsonObjectI.getString("coverImgUrl");
                        music_count=jsonObjectI.getInt("trackCount");
                        playlist_id=jsonObjectI.getLong("id");
                        Item_playlist item=new Item_playlist(playlist_name,playlist_url,music_count,playlist_id);
                        item_list.add(item);
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }
    }

}