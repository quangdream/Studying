package vn.edu.ptit.miniproject.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.adapter.PagerAdapter;
//import vn.edu.ptit.miniproject.fragment.FavoriteFragment;
import vn.edu.ptit.miniproject.model.ItemSaved;
import vn.edu.ptit.miniproject.sql.DataBaseUtil;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private DataBaseUtil dataBaseUtil;
    private ArrayList<ItemSaved> arrSaved;
    private ArrayList<ItemSaved> arrFavo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        dataBaseUtil = new DataBaseUtil(this);
        arrSaved = dataBaseUtil.getItems();
        arrFavo = dataBaseUtil.getLikedItems();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public PagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public DataBaseUtil getDataBaseUtil() {
        return dataBaseUtil;
    }

    public ArrayList<ItemSaved> getArrSaved() {
        return arrSaved;
    }

    public ArrayList<ItemSaved> getArrFavo() {
        return arrFavo;
    }
}
