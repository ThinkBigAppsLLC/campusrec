package edu.fsu.campusrec;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class StatusFragment extends Fragment {
    private SlidingUpPanelLayout slider;
    public StatusFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_status, container, false);
        MaterialListView statusListView = (MaterialListView) v.findViewById(R.id.status_list);

        for(Facility fac : MainActivity.facilities) {
            Card card;
            if(fac.getPhoto() != -1) {
                card = new Card.Builder(getContext())
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_image_with_buttons_card)
                        .setTitle(fac.getName())
                        .setTitleColor(Color.WHITE)
                        .setDescription(fac.getStatus())
                        .setDrawable(fac.getPhoto())
                        .endConfig()
                        .build();
            }
            else{
                card = new Card.Builder(getContext())
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_image_with_buttons_card)
                        .setTitle(fac.getName())
                        .setTitleColor(Color.WHITE)
                        .setDescription(fac.getStatus())
                        .endConfig()
                        .build();
            }
            statusListView.getAdapter().add(card);
        }

        slider = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_up_layout);
        slider.setPanelState(PanelState.HIDDEN);
        slider.setTouchEnabled(false);
        slider.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) { }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                switch (newState){
                    case EXPANDED:
                        setDetailSubtitle();
                        slider.setTouchEnabled(true);
                        break;
                    case COLLAPSED:
                        closePanel();
                        break;
                }
            }
        });

        statusListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                slider.setPanelState(PanelState.EXPANDED);
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {  }
        });
        return v;
    }

    private void setDetailSubtitle(){
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Details");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    public PanelState getSliderState(){
        if(slider != null)
            return slider.getPanelState();
        else
            return PanelState.HIDDEN;
    }

    public void closePanel(){
        if (slider != null){
            slider.setPanelState(PanelState.HIDDEN);
            slider.setTouchEnabled(false);
            if(((AppCompatActivity) getActivity()).getSupportActionBar() != null){
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(null);
            }
            ((MainActivity) getActivity()).toggle.syncState();
        }
    }
}
