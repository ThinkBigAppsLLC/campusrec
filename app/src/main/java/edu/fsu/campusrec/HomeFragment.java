package edu.fsu.campusrec;

/**
 * Home Module
 * Holds quick-status list of facilities
 * Holds latest Rainline tweet
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private View viewContainer;
    private CoordinatorLayout mCoord;
    private RecyclerView mRecyclerView;
    private StatusAdapter mStatusAdapter;

    public HomeFragment() { }

    //************************************
    // Lifecycle Methods
    //************************************

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContainer = inflater.inflate(R.layout.fragment_home, container, false);
        mCoord = (CoordinatorLayout) getActivity().findViewById(R.id.main_coordinator_layout);
        mRecyclerView = (RecyclerView) viewContainer.findViewById(R.id.recycler_status);

        ArrayList<String> bldgNames = new ArrayList<>();
        ArrayList<String> statuses = new ArrayList<>();
        Facility fac;
        for(int i = 0; i < MainActivity.facilities.size(); i++) {
            fac = MainActivity.facilities.get(i);
            bldgNames.add(fac.getName());
            statuses.add(fac.getStatus());
        }
        mStatusAdapter = new StatusAdapter(bldgNames, statuses, getContext());
        ContactFragment.CustomLinearLayoutManager mRecyclerManager = new ContactFragment.CustomLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mRecyclerManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mStatusAdapter);

        mStatusAdapter.setOnItemClickListener(new StatusAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                StatusFragment sFrag = StatusFragment.newInstance(MainActivity.facilities.get(position));
                FragmentTransaction fTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fTransaction.add(R.id.frag_container, sFrag);
                fTransaction.addToBackStack(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sFrag.setEnterTransition(new Slide());
                    sFrag.setExitTransition(new Slide());
                }
                else{
                    fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                fTransaction.commit();
            }
        });

        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(FacilityData.RAINLINE_USERNAME)
                .maxItemsPerRequest(1)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getContext())
                .setTimeline(userTimeline)
                .build();
        ((ListView) viewContainer.findViewById(R.id.tweet_rainline)).setAdapter(adapter);

        ImageView rainlinePhone = (ImageView) viewContainer.findViewById(R.id.phone_rainline);
        rainlinePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(FacilityData.RAINLINE_PHONE));
                getContext().startActivity(intent);
            }
        });

        ImageView fb = (ImageView) viewContainer.findViewById(R.id.connect_facebook);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri fbUri;
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    // Newer versions
                    if (versionCode >= 3002850) {
                        fbUri = FacilityData.CR_FACEBOOK_NEW;
                    } else { //older versions of fb app
                        fbUri = FacilityData.CR_FACEBOOK_OLD;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    fbUri = FacilityData.CR_FACEBOOK_WEB;
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(fbUri);
                startActivity(facebookIntent);
            }
        });

        ImageView tw = (ImageView) viewContainer.findViewById(R.id.connect_twitter);
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri twUri;
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    packageManager.getPackageInfo("com.twitter.android", 0);
                    twUri = FacilityData.CR_TWITTER;
                } catch (PackageManager.NameNotFoundException e) {
                    twUri = FacilityData.CR_TWITTER_WEB;
                }
                Intent twIntent = new Intent(Intent.ACTION_VIEW);
                twIntent.setData(twUri);
                startActivity(twIntent);
            }
        });

        ImageView ig = (ImageView) viewContainer.findViewById(R.id.connect_instagram);
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri igUri;
                Intent igIntent = new Intent(Intent.ACTION_VIEW);
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    packageManager.getPackageInfo("com.instagram.android", 0);
                    igIntent.setPackage("com.instagram.android");
                } catch (PackageManager.NameNotFoundException e) { }
                igUri = FacilityData.CR_INSTAGRAM;
                igIntent.setData(igUri);
                startActivity(igIntent);
            }
        });

        ImageView yt = (ImageView) viewContainer.findViewById(R.id.connect_youtube);
        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ytUri;
                Intent ytIntent = new Intent(Intent.ACTION_VIEW);
                PackageManager packageManager = getContext().getPackageManager();
                try {
                    packageManager.getPackageInfo("com.google.android.youtube", 0);
                    ytIntent.setPackage("com.google.android.youtube");
                } catch (PackageManager.NameNotFoundException e) { }
                ytUri = FacilityData.CR_YOUTUBE;
                ytIntent.setData(ytUri);
                startActivity(ytIntent);
            }
        });
        
        return viewContainer;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
