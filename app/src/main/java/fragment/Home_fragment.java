package fragment;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.netease_cloud_music.PlaylistActivity;
import com.example.netease_cloud_music.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Banner.ImageAdapter;
import NetRequest.GetNetRequest;

import NetRequest.MyJson;
import com.example.netease_cloud_music.SearchActivity;
import tool.GlideRoundTransform;


public class Home_fragment extends Fragment {
    private long[]id=new long[3];
    private String[]img=new String[3];
    private String[]name=new String[3];
    private GetNetRequest request=new GetNetRequest();
    private Mhandler mhandler=new Mhandler();
    private LinearLayout linearLayout;
    private ViewPager viewPager;
    private ImageHandler handler=new ImageHandler(new WeakReference<Home_fragment>(Home_fragment.this));
    private TextView recommed,text_1,text_2,text_3;
    private ImageView recommed_1,recommed_2,recommed_3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        link();
        recommed.getPaint().setFakeBoldText(true);
        recommed_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PlaylistActivity.class);
                intent.putExtra("playlist_id",id[0]);
                intent.putExtra("playlist_name",name[0]);
                intent.putExtra("playlist_cover",img[0]);
                getActivity().startActivity(intent);
            }
        });
        recommed_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PlaylistActivity.class);
                intent.putExtra("playlist_id",id[1]);
                intent.putExtra("playlist_name",name[1]);
                intent.putExtra("playlist_cover",img[1]);
                getActivity().startActivity(intent);
            }
        });
        recommed_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PlaylistActivity.class);
                intent.putExtra("playlist_id",id[2]);
                intent.putExtra("playlist_name",name[2]);
                intent.putExtra("playlist_cover",img[2]);
                getActivity().startActivity(intent);
            }
        });
        viewPager=(ViewPager)getActivity().findViewById(R.id.viewpager);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ImageView view1 = (ImageView) inflater.inflate(R.layout.item_banner, null);
        ImageView view2 = (ImageView) inflater.inflate(R.layout.item_banner, null);
        ImageView view3 = (ImageView) inflater.inflate(R.layout.item_banner, null);
        ImageView view4 = (ImageView) inflater.inflate(R.layout.item_banner, null);
        ImageView view5 = (ImageView) inflater.inflate(R.layout.item_banner, null);
        view1.setImageResource(R.drawable.banner_1);
        view2.setImageResource(R.drawable.banner_2);
        view3.setImageResource(R.drawable.banner_3);
        view4.setImageResource(R.drawable.banner_4);
        view5.setImageResource(R.drawable.banner_5);
        ArrayList<ImageView> views = new ArrayList<ImageView>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        viewPager.setAdapter(new ImageAdapter(views));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });

        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);

     linearLayout=(LinearLayout) getActivity().findViewById(R.id.search);
     linearLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getContext(), SearchActivity.class);
             getActivity().startActivity(intent);
         }
     });
    }

    public void link(){
        recommed=(TextView)getActivity().findViewById(R.id.recommended);
        text_1=(TextView)getActivity().findViewById(R.id.text_1);
        text_2=(TextView)getActivity().findViewById(R.id.text_2);
        text_3=(TextView)getActivity().findViewById(R.id.text_3);
        recommed_1=(ImageView)getActivity().findViewById(R.id.recommended_1);
        recommed_2=(ImageView)getActivity().findViewById(R.id.recommended_2);
        recommed_3=(ImageView)getActivity().findViewById(R.id.recommended_3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        request.sendGetRequest("http://sandyz.ink:3000/homepage/block/page",mhandler,1);
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);

    }


    private static class ImageHandler extends Handler {

        protected static final int MSG_UPDATE_IMAGE  = 1;

        protected static final int MSG_KEEP_SILENT   = 2;

        protected static final int MSG_BREAK_SILENT  = 3;

        protected static final int MSG_PAGE_CHANGED  = 4;

        protected static final long MSG_DELAY = 3000;    //轮播间隔时间

        private WeakReference<Home_fragment> weakReference;
        private int currentItem = 0;

        //使用弱引用避免Handler泄露.虽然不用好像也可以。。。

        protected ImageHandler(WeakReference<Home_fragment> wk){
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Home_fragment activity = weakReference.get();
            if (activity==null){
                return ;
            }

            if (activity.handler.hasMessages(MSG_UPDATE_IMAGE)){
                activity.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    activity.viewPager.setCurrentItem(currentItem);
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    break;
                case MSG_BREAK_SILENT:
                    activity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
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
                    JSONObject jsonObjectdata=jsonObject.getJSONObject("data");
                    JSONArray jsonArray=jsonObjectdata.getJSONArray("blocks");
                    JSONObject jsonObjects=jsonArray.getJSONObject(1);
                    JSONArray jsonArraylist=jsonObjects.getJSONArray("creatives");
                    for (int i = 1; i <4 ; i++) {
                        JSONObject jsonObjecti=jsonArraylist.getJSONObject(i);
                        id[i-1]=jsonObjecti.getLong("creativeId");
                        JSONObject jsonObjectui=jsonObjecti.getJSONObject("uiElement");
                        JSONObject jsonObjecttitle=jsonObjectui.getJSONObject("mainTitle");
                        JSONObject jsonObjectimg=jsonObjectui.getJSONObject("image");
                        name[i-1]=jsonObjecttitle.getString("title");
                        img[i-1]=jsonObjectimg.getString("imageUrl");

                    }
                    Glide.with(getActivity()).load(img[0]).transform(new GlideRoundTransform(getActivity(),10)).into(recommed_1);
                    Glide.with(getActivity()).load(img[1]).transform(new GlideRoundTransform(getActivity(),10)).into(recommed_2);
                    Glide.with(getActivity()).load(img[2]).transform(new GlideRoundTransform(getActivity(),10)).into(recommed_3);
                    text_1.setText(name[0]);
                    text_2.setText(name[1]);
                    text_3.setText(name[2]);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }



}