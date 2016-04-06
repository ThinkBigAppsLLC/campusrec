package edu.fsu.campusrec;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SliderLayout mainCarousel;
    private ExpandableListView statusELV;
    private ExpandableListAdapter statusLA;
    private ArrayList<String> statusList;
    private HashMap<String, List<String>> statusListChild;
    private CoordinatorLayout mCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoord = (CoordinatorLayout) findViewById(R.id.coord);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        mainCarousel = (SliderLayout) findViewById(R.id.main_carousel);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("OGA at the Rez", "http://campusrec.fsu.edu/sites/default/files/4.16_RezYoga_AJ_0.jpg");
        url_maps.put("April Outdoor Pursuits", "http://campusrec.fsu.edu/sites/default/files/4.1_April_OP_web_DR.jpg");
        url_maps.put("Drop It Like It's Hot", "http://campusrec.fsu.edu/sites/default/files/4.7_drop-it-hot_web_ER.jpg");
        url_maps.put("Sunset at the Rez", "http://campusrec.fsu.edu/sites/default/files/4.13_Sunset_Rez_web_DR.jpg");
        for(String name : url_maps.keySet()){
            DefaultSliderView slider = new DefaultSliderView(this);
            // initialize a SliderLayout
            slider.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);

            mainCarousel.addSlider(slider);
            mainCarousel.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            mainCarousel.setDuration(5000);
        }

        statusELV = (ExpandableListView) findViewById(R.id.status_list_view);
        prepareListData();
        statusLA = new ExpandableListAdapter(this, statusList, statusListChild);
        statusELV.setAdapter(statusLA);
        statusELV.setGroupIndicator(getDrawable(R.drawable.selector_status));

        statusELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        statusELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Snackbar.make(mCoord, "You have expanded group " + groupPosition + ".", Snackbar.LENGTH_LONG);
            }
        });

        statusELV.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Snackbar.make(mCoord, "You have collapsed group " + groupPosition + ".", Snackbar.LENGTH_LONG);
            }
        });

        statusELV.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Snackbar.make(mCoord, "You have collapsed child " + childPosition + ".", Snackbar.LENGTH_LONG);
                return false;
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        mainCarousel.startAutoCycle();
    }

    @Override
    protected void onPause(){
        mainCarousel.stopAutoCycle();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                break;
            case R.id.nav_status:
                break;
            case R.id.nav_calendar:
                break;
            case R.id.nav_im:
                break;
            case R.id.nav_res:
                break;
            case R.id.nav_contact:
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //*************************************************************
    // Helper Functions
    //*************************************************************

    // Preparing list data to show in expListView
    private void prepareListData() {
        String[] data = getResources().getStringArray(R.array.status_headers);
        statusList = new ArrayList<>(Arrays.asList(data));
        statusListChild = new HashMap<>();

        List<String> leachHours = new ArrayList<>();
        leachHours.add("M-Th | 6.00a - 11.00p");
        leachHours.add("F | 6.00a - 10.00p");
        leachHours.add("Sat-Sun | 11.00a - 9.00p");

        List<String> rezHours = new ArrayList<>();
        rezHours.add("M-Th | 2.00p - 7.00p");
        rezHours.add("F-Sun | 12.00p - 7.00p");

        List<String> fmcHours = new ArrayList<>();
        fmcHours.add("M-F | 6.00a - 9.00p");
        fmcHours.add("Sat-Sun | 8.00a - 5.00p");

        List<String> rspHours = new ArrayList<>();
        rspHours.add("Sun-Th | 5.30p - 10.30p");
        rspHours.add("F-Sat | Special Events Only");

        List<String> mcfHours = new ArrayList<>();
        mcfHours.add("M-F | until 10.00p");
        mcfHours.add("Sat-Sun | until 9.00p");

        List<String> wscHours = new ArrayList<>();
        wscHours.add("M-F | until 10.00p");
        wscHours.add("Sat-Sun | until 9.00p");

        statusListChild.put(statusList.get(0), leachHours);
        statusListChild.put(statusList.get(1), rezHours);
        statusListChild.put(statusList.get(2), fmcHours);
        statusListChild.put(statusList.get(3), rspHours);
        statusListChild.put(statusList.get(4), mcfHours);
        statusListChild.put(statusList.get(5), wscHours);
    }
}
