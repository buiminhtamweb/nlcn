package mycompany.com.nlcn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import mycompany.com.nlcn.Adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener {

    private TextView mTextMessage;
    private ViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private MenuItem mPrevMenuItem;
    private BottomNavigationView mBottomNavigation;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
//        mToolbar = findViewById(R.id.toolbar);
//        mToolbar.setTitle("Trang chủ");

        mBottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTextMessage = (TextView) findViewById(R.id.message);

        mBottomNavigation.setOnNavigationItemSelectedListener(this);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mViewPager.setCurrentItem(0, true);
                return true;
            case R.id.navigation_dashboard:
                mViewPager.setCurrentItem(1, true);
                return true;
            case R.id.navigation_notifications:
                mViewPager.setCurrentItem(2, true);
                return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPrevMenuItem != null) {
            mPrevMenuItem.setChecked(false);
        } else {
            mBottomNavigation.getMenu().getItem(0).setChecked(false);
        }
        Log.d("page", "onPageSelected: " + position);
        mBottomNavigation.getMenu().getItem(position).setChecked(true);
        mPrevMenuItem = mBottomNavigation.getMenu().getItem(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = getIntent().getExtras();
        if (null!=bundle){
            int position = bundle.getInt("position", 0);
            mViewPager.setCurrentItem(position);
        }

    }
}
