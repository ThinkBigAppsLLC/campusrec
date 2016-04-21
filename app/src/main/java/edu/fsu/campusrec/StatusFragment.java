package edu.fsu.campusrec;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class StatusFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {
    private final static String LEACH_FMC_URL = "http://campusrec.fsu.edu/fitness/leach-fmc";

    private MapView map;
    private GoogleMap gMap;
    private Facility fac;
    private View mainContainer;

    //*****************************************
    // Constructors
    //*****************************************

    public StatusFragment() { }

    public static StatusFragment newInstance(Facility fac){
        StatusFragment sFrag = new StatusFragment();
        sFrag.setBuilding(fac);
        return sFrag;
    }

    //*****************************************
    // Fragment Lifecycle
    //*****************************************

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_status_detail, container, false);
        mainContainer = v;

        // Change to active facility
        if(fac == null)
            return v;
        Facility activeFac = fac;

        // Create MapView
        map = (MapView) v.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        TextView day;
        TextView label;
        TextView details = (TextView) v.findViewById(R.id.details);

        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        // DAILY HOURS FACTORY
        HashMap<String, String> opHours = activeFac.getHours();
        day = (TextView) v.findViewById(R.id.hours_sun);
        day.setText(opHours.get("sat"));
        if(dayOfWeek == Calendar.SUNDAY) {
            label = (TextView) v.findViewById(R.id.hours_sun_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_mon);
        day.setText(opHours.get("mon"));
        if(dayOfWeek == Calendar.MONDAY) {
            label = (TextView) v.findViewById(R.id.hours_mon_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_tues);
        day.setText(opHours.get("tues"));
        if(dayOfWeek == Calendar.TUESDAY) {
            label = (TextView) v.findViewById(R.id.hours_tues_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_wed);
        day.setText(opHours.get("wed"));
        if(dayOfWeek == Calendar.WEDNESDAY) {
            label = (TextView) v.findViewById(R.id.hours_wed_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_thurs);
        day.setText(opHours.get("thurs"));
        if(dayOfWeek == Calendar.THURSDAY) {
            label = (TextView) v.findViewById(R.id.hours_thurs_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_fri);
        day.setText(opHours.get("fri"));
        if(dayOfWeek == Calendar.FRIDAY) {
            label = (TextView) v.findViewById(R.id.hours_fri_label);
            setCurrentDayText(label, day);
        }

        day = (TextView) v.findViewById(R.id.hours_sat);
        day.setText(opHours.get("sat"));
        if(dayOfWeek == Calendar.SATURDAY) {
            label = (TextView) v.findViewById(R.id.hours_sat_label);
            setCurrentDayText(label, day);
        }

        TextView adr = (TextView) v.findViewById(R.id.directionAddress);
        adr.setText(fac.getAddress());

        if(fac.getNumber() != null){
            TextView phone = (TextView) v.findViewById(R.id.phoneNumber);
            phone.setText(fac.getNumber());
        }
        else{
            v.findViewById(R.id.divider).setVisibility(View.GONE);
            v.findViewById(R.id.phoneRibbon).setVisibility(View.GONE);
        }

        // Set Details
        int det = -1;
        switch(activeFac.getBldg()){
            case LEACH:
                det = R.string.details_leach;
                break;
            case REZ:
                det = R.string.details_rez;
                break;
            case FMC:
                det = R.string.details_fmc;
                break;
            case RSP:
                det = R.string.details_rsp;
                break;
            case MCF:
                det = R.string.details_mcf;
                break;
            case WSC:
                det = R.string.details_wsc;
                break;
        }
        details.setText(Html.fromHtml(getContext().getString(det)));

        // Set TopBar Title
        setActionBarTitle(activeFac.getName());

        return v;
    }

    @Override
    public void onDestroy(){
        ((MainActivity) getActivity()).restoreTitle();
        super.onDestroy();
    }

    //*****************************************
    // Google Api Client Required Methods
    //*****************************************

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //*****************************************
    // Google Maps Api Required Methods
    //*****************************************

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.addMarker(new MarkerOptions().position(fac.getLoc()));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fac.getLoc(), 15));
        map.onResume();
    }

    //*****************************************
    // Helper Functions
    //*****************************************

    private void setCurrentDayText(TextView label, TextView hours){
        label.setTextSize(15);
        label.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        hours.setTextSize(15);
        hours.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        //hours.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @SuppressWarnings("ConstantConditions")
    private void setActionBarTitle(String title){
        if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setBuilding(@NonNull Facility fac){
        this.fac = fac;
    }

    private class GetDetails extends AsyncTask<String, Void, String> {
        private Facility.Building bldg;

        public GetDetails(Facility.Building bldg){
            this.bldg = bldg;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            for (String url : urls) {
                try {
                    Document doc  = Jsoup.connect(url).get();
                    Elements classElements = doc.getElementsByClass("even");
                    Elements children = classElements.first().children();
                    Element beginLeach = classElements.select("p:contains(The Bobby E. Leach)").first();
                    int beginLeachIndex = classElements.first().children().indexOf(beginLeach);
                    classElements.first().getElementsByIndexLessThan(beginLeachIndex).remove();
                    for (Element tmp : classElements){
                        if(tmp.hasText())
                            result.append(tmp.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            ((TextView) mainContainer.findViewById(R.id.details)).setText(Html.fromHtml(result));
        }
    }

}
