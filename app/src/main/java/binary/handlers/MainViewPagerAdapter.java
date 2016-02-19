package binary.handlers;



import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell pc on 9/26/2015.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mfraglist = new ArrayList<>();
    private final List<String> mfragtitlelist = new ArrayList<>();


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mfraglist.get(position);
    }

    @Override
    public int getCount() {
        return mfraglist.size();
    }

    public void addfragment(Fragment fragment,String title){
        mfraglist.add(fragment);
        mfragtitlelist.add(title);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mfragtitlelist.get(position);

    }
}
