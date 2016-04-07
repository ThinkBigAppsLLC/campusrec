package edu.fsu.campusrec;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;

public class StatusFragment extends Fragment {

    public StatusFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);
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

        statusListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fTrans = fragmentManager.beginTransaction();
                fTrans.add(R.id.frag_container, new StatusDetailFragment());
                fTrans.addToBackStack("detail");
                fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fTrans.commit();
                try {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Details");
                }
                catch (NullPointerException npe){
                    npe.printStackTrace();
                }

            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {

            }
        });
        return v;
    }

}
