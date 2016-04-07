package edu.fsu.campusrec;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;

public class StatusFragment extends Fragment {

    public StatusFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        MaterialListView statusListView = (MaterialListView) v.findViewById(R.id.status_list);

        for(String bldg : getResources().getStringArray(R.array.status_headers)) {
            Card card = new Card.Builder(getContext())
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_image_with_buttons_card)
                    .setTitle(bldg)
                    .setTitleColor(Color.WHITE)
                    .setDescription("STATUS")
                    .setDrawable(R.drawable.leach_photo)
                    .endConfig()
                    .build();
            statusListView.getAdapter().add(card);
        }
        return v;
    }

}
