package Banner;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {

    private ArrayList<ImageView> viewlist;

    public ImageAdapter(ArrayList<ImageView> viewlist) {
        this.viewlist = viewlist;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;    //设置成最大整数，使用户看不到边界
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 对ViewPager页号求模取出View列表中要显示的项
        position %= viewlist.size();
        if (position < 0) {
            position = viewlist.size() + position;
        }
        ImageView view = viewlist.get(position);

        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }
}

