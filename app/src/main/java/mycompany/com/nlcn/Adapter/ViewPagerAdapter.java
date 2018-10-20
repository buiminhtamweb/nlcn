package mycompany.com.nlcn.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import mycompany.com.nlcn.Fragment.GioHangFrag;
import mycompany.com.nlcn.Fragment.HomeFrag;
import mycompany.com.nlcn.Fragment.UserFrag;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mChildFrags;
    private String[] mTitle;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mChildFrags = new Fragment[]{new HomeFrag(), new GioHangFrag(), new UserFrag()};
        mTitle = new String[]{"Trang chủ", "Đặt hàng", "Tài khoản"};
    }

    @Override
    public Fragment getItem(int position) {
        return mChildFrags[position];
    }

    @Override
    public int getCount() {
        return mChildFrags.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
