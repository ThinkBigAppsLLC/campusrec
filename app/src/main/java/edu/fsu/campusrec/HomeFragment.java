package edu.fsu.campusrec;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private static final String RAINLINE_USERNAME = "fsuimrainline";
    private static final String RAINLINE_PHONE = "tel:8506457246";

    private View viewContainer;

    private SliderLayout mainCarousel;
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
                Log.i("StatusAdapter", "You clicked!");
            }
        });


        // CAROUSEL
        mainCarousel = (SliderLayout) viewContainer.findViewById(R.id.main_carousel);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("OGA at the Rez", "http://campusrec.fsu.edu/sites/default/files/4.16_RezYoga_AJ_0.jpg");
        url_maps.put("April Outdoor Pursuits", "http://campusrec.fsu.edu/sites/default/files/4.1_April_OP_web_DR.jpg");
        url_maps.put("Drop It Like It's Hot", "http://campusrec.fsu.edu/sites/default/files/4.7_drop-it-hot_web_ER.jpg");
        url_maps.put("Sunset at the Rez", "http://campusrec.fsu.edu/sites/default/files/4.13_Sunset_Rez_web_DR.jpg");
        for(String name : url_maps.keySet()){
            DefaultSliderView slider = new DefaultSliderView(getContext());
            slider.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            mainCarousel.addSlider(slider);
            mainCarousel.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            mainCarousel.setDuration(5000);
        }

        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(RAINLINE_USERNAME)
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
                intent.setData(Uri.parse(RAINLINE_PHONE));
                getContext().startActivity(intent);
            }
        });


        return viewContainer;
    }

    @Override
    public void onResume(){
        super.onResume();
        mainCarousel.startAutoCycle();
    }

    @Override
    public void onPause(){
        mainCarousel.stopAutoCycle();
        super.onPause();
    }

    //************************************
    // Helper Methods
    //************************************
}
