package edu.fsu.campusrec;

/**
 * Calendar Module
 * Holds all Calendar/Group Event Handling
 * Currently empty - waiting for client data
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CalendarFragment extends Fragment {


    public CalendarFragment() {    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

}
