package edu.fsu.campusrec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fManager;
    private HomeFragment hFrag;
    private ReservationFragment resFrag;
    private CalendarFragment calFrag;
    private ContactFragment contactFrag;
    private StatusFragment statusFrag;

    public ActionBarDrawerToggle toggle;

    public static ArrayList<Facility> facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        facilities = new ArrayList<>();
        prepareListData();

        fManager = getSupportFragmentManager();
        if(hFrag == null)
            hFrag = new HomeFragment();
        if(calFrag == null)
            calFrag = new CalendarFragment();
        if(contactFrag == null)
            contactFrag = new ContactFragment();
        if(statusFrag == null)
            statusFrag = new StatusFragment();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.frag_container, hFrag);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fTransaction.commit();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getSupportFragmentManager().getBackStackEntryCount() > 1){
                        Log.i("FRAG MAN", "POPPING!");
                        getSupportFragmentManager().popBackStack();
                        restoreTitle();
                    }
                    else if(drawer != null){
                        if(!drawer.isDrawerOpen(GravityCompat.START)){
                            drawer.openDrawer(GravityCompat.START);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume(){
        super.onResume();
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

    public void restoreTitle(){
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setSubtitle(null);
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Intent browserIntent;
        int id = item.getItemId();
        switch (id){
            case R.id.nav_home:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Campus Rec");
                    getSupportActionBar().setSubtitle(null);
                    fManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fTransaction.replace(R.id.frag_container, hFrag);
                break;
            case R.id.nav_status:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Status");
                    getSupportActionBar().setSubtitle(null);
                    fManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fTransaction.replace(R.id.frag_container, statusFrag);
                break;
            case R.id.nav_calendar:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Calendar");
                    getSupportActionBar().setSubtitle(null);
                    fManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fTransaction.replace(R.id.frag_container, calFrag);
                break;
            case R.id.nav_im:
                browserIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.teamsideline.com/fsu"));
                startActivity(browserIntent);
                break;
            case R.id.nav_res:
/*                if(resFrag == null)
                    resFrag = new ReservationFragment();
                fTransaction.replace(R.id.frag_container, resFrag);*/
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fsucr.setmore.com/"));
                startActivity(browserIntent);
                break;
            case R.id.nav_contact:
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Contact Us");
                    getSupportActionBar().setSubtitle(null);
                    fManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fTransaction.replace(R.id.frag_container, contactFrag);
                break;
            default:
                break;
        }

        fTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkOpen(Facility.Building bldg){
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        switch(bldg){
            case LEACH:
                switch(day){
                    case Calendar.SUNDAY:
                    case Calendar.SATURDAY:
                        if(hour < 11 || hour > 21)
                            return false;
                        break;
                    case Calendar.MONDAY:
                    case Calendar.TUESDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                        if(hour < 6 || hour > 23)
                            return false;
                        break;
                    case Calendar.FRIDAY:
                        if(hour < 6 || hour > 22)
                            return false;
                        break;
                }
                break;
            case REZ:
                switch(day){
                    case Calendar.FRIDAY:
                    case Calendar.SATURDAY:
                    case Calendar.SUNDAY:
                        if(hour < 12 || hour > 19)
                            return false;
                        break;
                    case Calendar.MONDAY:
                    case Calendar.TUESDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                        if(hour < 14 || hour > 19)
                            return false;
                        break;
                }
                break;
            case FMC:
                switch(day){
                    case Calendar.FRIDAY:
                    case Calendar.SATURDAY:
                    case Calendar.SUNDAY:
                        if(hour < 12 || hour > 19)
                            return false;
                        break;
                    case Calendar.MONDAY:
                    case Calendar.TUESDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                        if(hour < 14 || hour > 19)
                            return false;
                        break;
                }
                break;
            case RSP:
                switch(day){
                    case Calendar.FRIDAY:
                    case Calendar.SATURDAY:
                        return false;
                    case Calendar.SUNDAY:
                    case Calendar.MONDAY:
                    case Calendar.TUESDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                        if(hour < 17 || hour > 22)
                            return false;
                        else if (hour == 17){
                            if (min < 30)
                                return false;
                        }
                        else if (hour == 22){
                            if (min > 30)
                                return false;
                        }
                        break;
                }
                break;
            case MCF:
            case WSC:
                switch(day){
                    case Calendar.SATURDAY:
                    case Calendar.SUNDAY:
                        if(hour > 21)
                            return false;
                    case Calendar.MONDAY:
                    case Calendar.TUESDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.THURSDAY:
                    case Calendar.FRIDAY:
                        if(hour > 22)
                            return false;
                }
                break;
        }
        return true;
    }

    private void prepareListData() {
        ArrayList<Facility.opHours> facilityOpHours;
        Facility.opHours.Hours open;
        Facility.opHours.Hours close;
        Facility.opHours opHours;

        // ********************
        // LEACH
        // ********************

        facilityOpHours = new ArrayList<>();

        // LEACH SUNDAY
        open = new Facility.opHours.Hours(11, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(9, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        // LEACH MONDAY - THURSDAY
        open = new Facility.opHours.Hours(6, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(11, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        // LEACH FRIDAY
        open = new Facility.opHours.Hours(11, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(10, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        // LEACH SATURDAY
        open = new Facility.opHours.Hours(11, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(9, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        Facility leachRC = new Facility(Facility.Building.LEACH, R.drawable.leach_photo, facilityOpHours, checkOpen(Facility.Building.LEACH));
        facilities.add(leachRC);

        // ********************
        // REZ
        // ********************

        facilityOpHours = new ArrayList<>();

        // REZ SUNDAY
        open = new Facility.opHours.Hours(12, 0, Facility.opHours.Hours.AMPM.PM);
        close = new Facility.opHours.Hours(7, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        // REZ MONDAY - THURSDAY
        open = new Facility.opHours.Hours(2, 0, Facility.opHours.Hours.AMPM.PM);
        close = new Facility.opHours.Hours(7, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        // REZ FRIDAY/SATURDAY
        open = new Facility.opHours.Hours(12, 0, Facility.opHours.Hours.AMPM.PM);
        close = new Facility.opHours.Hours(7, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        Facility rez = new Facility(Facility.Building.REZ, R.drawable.rez_photo, facilityOpHours, checkOpen(Facility.Building.REZ));
        facilities.add(rez);

        // ********************
        // FMC
        // ********************

        facilityOpHours = new ArrayList<>();

        // SUNDAY
        open = new Facility.opHours.Hours(8, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(5, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        // MONDAY - FRIDAY
        open = new Facility.opHours.Hours(6, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(9, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        // SATURDAY
        open = new Facility.opHours.Hours(8, 0, Facility.opHours.Hours.AMPM.AM);
        close = new Facility.opHours.Hours(5, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);

        Facility fmc = new Facility(Facility.Building.FMC, R.drawable.fmc_photo, facilityOpHours, checkOpen(Facility.Building.REZ));
        facilities.add(fmc);

        // ********************
        // RSP
        // ********************

        facilityOpHours = new ArrayList<>();

        // SUNDAY - THURSDAY
        open = new Facility.opHours.Hours(5, 30, Facility.opHours.Hours.AMPM.PM);
        close = new Facility.opHours.Hours(10, 30, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(open, close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        // FRI/SAT
        opHours = new Facility.opHours(Facility.opHours.Special.EVENTS);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        Facility rsp = new Facility(Facility.Building.RSP, R.drawable.rsp_photo, facilityOpHours, checkOpen(Facility.Building.RSP));
        facilities.add(rsp);

        // ********************
        // MCF
        // ********************

        facilityOpHours = new ArrayList<>();

        // SUN
        close = new Facility.opHours.Hours(9, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(close);
        facilityOpHours.add(opHours);

        // MON - FRI
        close = new Facility.opHours.Hours(10, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(close);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);
        facilityOpHours.add(opHours);

        // SAT
        close = new Facility.opHours.Hours(9, 0, Facility.opHours.Hours.AMPM.PM);
        opHours = new Facility.opHours(close);
        facilityOpHours.add(opHours);

        Facility mcf = new Facility(Facility.Building.MCF, R.drawable.mcf_photo, facilityOpHours, checkOpen(Facility.Building.MCF));
        facilities.add(mcf);

        // ********************
        // WSC
        // ********************
        Facility wsc = new Facility(Facility.Building.WSC, -1, facilityOpHours, checkOpen(Facility.Building.WSC));
        facilities.add(wsc);


    }


}
