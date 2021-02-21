package Search_bar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.netease_cloud_music.R;

import java.util.List;

public class RecordsAdapter extends ArrayAdapter {

    private int resourceId;

    public RecordsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView showRecord = (TextView)view.findViewById(R.id.text_record);
        String record = (String)getItem(position);
        showRecord.setText(record);
        return view;
    }
}
