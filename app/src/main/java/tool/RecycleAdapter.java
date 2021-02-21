package tool;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netease_cloud_music.R;

import com.example.netease_cloud_music.PlaylistActivity;
import java.util.List;

import Item.Item_playlist;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
      private List<Item_playlist>list;
      private Context mcontext;
      public RecycleAdapter(Context mcontext,List<Item_playlist>list){
          this.list=list;
          this.mcontext=mcontext;
      }
    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.playlistview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                Item_playlist item_playlist=list.get(position);
                Intent intent=new Intent(mcontext,PlaylistActivity.class);
                intent.putExtra("playlist_id",item_playlist.getPlaylist_id());
                intent.putExtra("playlist_name",item_playlist.getName_playlist());
                intent.putExtra("playlist_cover",item_playlist.getUrl_playlist());
                mcontext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
        Item_playlist item=list.get(position);
        holder.playlist_name.setText(item.getName_playlist());
        holder.playlist_count.setText(""+item.getCount_music()+"首");
        Glide.with(mcontext).load(item.getUrl_playlist()).transform(new GlideRoundTransform(mcontext,8)).into(holder.playlist_img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
          View playlistview;
        TextView playlist_name;
        ImageView playlist_img;
        TextView playlist_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistview=itemView;
            playlist_name=(TextView)itemView.findViewById(R.id.text_playlist);
            playlist_img=(ImageView)itemView.findViewById(R.id.img_playlist);
            playlist_count=(TextView)itemView.findViewById(R.id.text_playlist_count);
        }
    }
}
