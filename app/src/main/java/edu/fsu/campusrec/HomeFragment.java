package edu.fsu.campusrec;

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
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private View viewContainer;

    private SliderLayout mainCarousel;
    private ExpandableListView statusELV;
    private ExpandableListAdapter statusLA;
    private ArrayList<String> statusList;
    private HashMap<String, List<String>> statusListChild;
    private CoordinatorLayout mCoord;
    private RecyclerView mRecyclerView;
    private StatusAdapter mStatusAdapter;

    public HomeFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewContainer = inflater.inflate(R.layout.fragment_home, container, false);

        prepareListData();
        mCoord = (CoordinatorLayout) getActivity().findViewById(R.id.main_coordinator_layout);
        mRecyclerView = (RecyclerView) viewContainer.findViewById(R.id.recycler_status);
        mStatusAdapter = new StatusAdapter(statusList, getContext());
        LinearLayoutManager mRecyclerManager = new LinearLayoutManager(getContext());
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
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);

            mainCarousel.addSlider(slider);
            mainCarousel.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            mainCarousel.setDuration(5000);
        }

        // LIST VIEW
/*
        statusELV = (ExpandableListView) viewContainer.findViewById(R.id.status_list_view);
        prepareListData();
        statusLA = new ExpandableListAdapter(getContext(), statusList, statusListChild);
        statusELV.setAdapter(statusLA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusELV.setGroupIndicator(getContext().getDrawable(R.drawable.selector_status));
        }
*/

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

    //*************************************************************
    // Helper Functions
    //*************************************************************

    private void prepareListData() {
        String[] data = getResources().getStringArray(R.array.status_headers);
        statusList = new ArrayList<>(Arrays.asList(data));
        statusListChild = new HashMap<>();

        List<String> leachHours = new ArrayList<>();
        leachHours.add("S | 11.00a - 9.00p");
        leachHours.add("M | 6.00a - 11.00p");
        leachHours.add("T | 6.00a - 11.00p");
        leachHours.add("W | 6.00a - 11.00p");
        leachHours.add("T | 6.00a - 11.00p");
        leachHours.add("F | 6.00a - 10.00p");
        leachHours.add("S | 11.00a - 9.00p");

        List<String> rezHours = new ArrayList<>();
        rezHours.add("S | 12.00p - 7.00p");
        rezHours.add("M | 2.00p - 7.00p");
        rezHours.add("T | 2.00p - 7.00p");
        rezHours.add("W | 2.00p - 7.00p");
        rezHours.add("T | 2.00p - 7.00p");
        rezHours.add("F | 12.00p - 7.00p");
        rezHours.add("S | 12.00p - 7.00p");

        List<String> fmcHours = new ArrayList<>();
        fmcHours.add("S | 8.00a - 5.00p");
        fmcHours.add("M | 6.00a - 9.00p");
        fmcHours.add("T | 6.00a - 9.00p");
        fmcHours.add("W | 6.00a - 9.00p");
        fmcHours.add("T | 6.00a - 9.00p");
        fmcHours.add("F | 6.00a - 9.00p");
        fmcHours.add("S | 8.00a - 5.00p");

        List<String> rspHours = new ArrayList<>();
        rspHours.add("S | 5.30p - 10.30p");
        rspHours.add("M | 5.30p - 10.30p");
        rspHours.add("T | 5.30p - 10.30p");
        rspHours.add("W | 5.30p - 10.30p");
        rspHours.add("T | 5.30p - 10.30p");
        rspHours.add("F | Special Events Only");
        rspHours.add("S | Special Events Only");

        List<String> mcfHours = new ArrayList<>();
        mcfHours.add("S | until 9.00p");
        mcfHours.add("M | until 10.00p");
        mcfHours.add("T | until 10.00p");
        mcfHours.add("W | until 10.00p");
        mcfHours.add("T | until 10.00p");
        mcfHours.add("F | until 10.00p");
        mcfHours.add("S | until 9.00p");

        List<String> wscHours = new ArrayList<>();
        wscHours.add("S | until 9.00p");
        wscHours.add("M | until 10.00p");
        wscHours.add("T | until 10.00p");
        wscHours.add("W | until 10.00p");
        wscHours.add("T | until 10.00p");
        wscHours.add("F | until 10.00p");
        wscHours.add("S | until 9.00p");

        statusListChild.put(statusList.get(0), leachHours);
        statusListChild.put(statusList.get(1), rezHours);
        statusListChild.put(statusList.get(2), fmcHours);
        statusListChild.put(statusList.get(3), rspHours);
        statusListChild.put(statusList.get(4), mcfHours);
        statusListChild.put(statusList.get(5), wscHours);
    }

}
