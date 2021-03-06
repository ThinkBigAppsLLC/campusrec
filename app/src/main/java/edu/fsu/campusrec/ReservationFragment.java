package edu.fsu.campusrec;

/**
 * Reservation Module
 * Currently unused - current functionality in launching external web intent
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class ReservationFragment extends Fragment {
    private static final String SetMoreUrl = "http://fsucr.setmore.com/";

    private WebView webView;

    public ReservationFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reservation, container, false);
        webView = (WebView) v.findViewById(R.id.reservationWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.loadUrl(SetMoreUrl);

        return v;
    }

}
