package tool;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netease_cloud_music.Music_play_Activity;
import com.example.netease_cloud_music.R;

import java.io.Serializable;
import java.util.List;

import Item.Item_music;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Item_music>list;
    private Context mcontext;

    public SearchAdapter(Context mcontext,List<Item_music>list){
        this.list=list;
        this.mcontext=mcontext;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item_music item=list.get(position);
        holder.music_name.setText(item.getMusic_name());
        holder.music_singer.setText(item.getMusic_singer());
        holder.music_album.setText(item.getMusic_album());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.musicview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Item_music item=list.get(position);
                Intent intent=new Intent(mcontext, Music_play_Activity.class);
                intent.putExtra("music_list",(Serializable)list);
                intent.putExtra("position",position);
                intent.putExtra("music_id",item.getMusic_id());
                intent.putExtra("music_img",item.getMusic_url());
                mcontext.startActivity(intent);
            }
        });
        return viewHolder;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicview;
        TextView music_name,music_singer,music_album;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicview=itemView;
            music_name=(TextView)itemView.findViewById(R.id.music_name1);
            music_singer=(TextView)itemView.findViewById(R.id.music_singer1);
            music_album=(TextView)itemView.findViewById(R.id.music_album1);
        }
    }
}

