package edu.fsu.campusrec;

import android.content.Intent;
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

    //************************************
    // Helper Methods
    //************************************
}
