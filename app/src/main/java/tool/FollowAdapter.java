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
import com.example.netease_cloud_music.PlaylistActivity;
import com.example.netease_cloud_music.R;

import java.util.List;

import Item.Item_follow;
import Item.Item_playlist;

public class FollowAdapter  extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
    private List<Item_follow> list;
    private Context mcontext;

    public FollowAdapter(Context mcontext,List<Item_follow> list) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow, parent, false);
       ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowAdapter.ViewHolder holder, int position) {
        Item_follow item = list.get(position);
        holder.name.setText(item.getName());
        Glide.with(mcontext).load(item.getAvatar()).transform(new GlideCircleTransform(mcontext)).crossFade().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.follow_name);
            avatar = (ImageView) itemView.findViewById(R.id.follow_avatar);
        }
    }
}
