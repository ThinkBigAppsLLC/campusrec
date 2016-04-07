package edu.fsu.campusrec;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class ReservationFragment extends Fragment {
    private WebView webView;

    public ReservationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, container, false);
        webView = (WebView) v.findViewById(R.id.reservationWebView);
        return v;
    }

}
