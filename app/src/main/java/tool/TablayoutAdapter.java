package tool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TablayoutAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    List<String> fragmenttitle;
    public TablayoutAdapter(FragmentManager fm, int behavior, List<Fragment> fragmentList,List<String> fragmenttitle){
        super(fm,behavior);
        this.fragmentList=fragmentList;
        this.fragmenttitle=fragmenttitle;
    }
    @NonNull
    public Fragment getItem(int position){
        return fragmentList.get(position);
    }
    public int getCount(){
        return fragmentList.size();
    }
    public CharSequence getPageTitle(int position) {
        return fragmenttitle.get(position);
    }
}
