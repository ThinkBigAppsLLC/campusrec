package edu.fsu.campusrec;

/**
 * Status Detail Module
 * Used to hold detailed information about each facility
 * Accessed from the quick-status list.
 */


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;

public class StatusFragment extends Fragment implements OnMapReadyCallback {
    private final static String LEACH_FMC_URL = "http://campusrec.fsu.edu/fitness/leach-fmc";

    private MapView map;
    private Facility fac;

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

        // Change to active facility
        if(fac == null)
            return v;

        // Create MapView
        map = (MapView) v.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        TextView day;
        TextView label;

        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        // DAILY HOURS FACTORY
        HashMap<String, String> opHours = fac.getHours();
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

        TextView adr = (TextView) v.findViewById(R.id.direction);
        adr.setText(fac.getAddress());
        RelativeLayout directionRibbon = (RelativeLayout) v.findViewById(R.id.ribbon_direction);
        directionRibbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng loc = fac.getLoc();
                Uri directionUri = Uri.parse("google.navigation:q=" + loc.latitude + "," + loc.longitude);
                Intent directionIntent = new Intent(Intent.ACTION_VIEW, directionUri);
                directionIntent.setPackage("com.google.android.apps.maps");
                if (directionIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(directionIntent);
                }
            }
        });

        if(fac.getNumber() == null|| !MainActivity.canCall()){
            v.findViewById(R.id.divider_contact).setVisibility(View.GONE);
            v.findViewById(R.id.ribbon_phone).setVisibility(View.GONE);
        }
        else{
            TextView phone = (TextView) v.findViewById(R.id.phone);
            phone.setText(fac.getNumber());
            final RelativeLayout phoneRibbon = (RelativeLayout) v.findViewById(R.id.ribbon_phone);
            phoneRibbon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + fac.getNumber().replaceAll("\\.", "")));
                        getContext().startActivity(intent);
                    }
                    catch (ActivityNotFoundException anfe){
                        Snackbar.make(phoneRibbon, "Your device cannot handle phone calls.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

        EnumSet<FacilityData.Amenities> amens = fac.getAmenities();

        for (FacilityData.Amenities amen : amens) {
            switch (amen){
                case WIFI:
                    v.findViewById(R.id.container_wifi).setVisibility(View.VISIBLE);
                    break;
                case RESERVE:
                    v.findViewById(R.id.container_reserve).setVisibility(View.VISIBLE);
                    break;
                case EQUIPMENT:
                    v.findViewById(R.id.container_equipment).setVisibility(View.VISIBLE);
                    break;
                case RUN:
                    v.findViewById(R.id.container_run).setVisibility(View.VISIBLE);
                    break;
                case BIKE:
                    v.findViewById(R.id.container_bike).setVisibility(View.VISIBLE);
                    break;
                case SWIM:
                    v.findViewById(R.id.container_swim).setVisibility(View.VISIBLE);
                    break;
                case ROW:
                    v.findViewById(R.id.container_row).setVisibility(View.VISIBLE);
                    break;
                case FIELD:
                    v.findViewById(R.id.container_field).setVisibility(View.VISIBLE);
                    break;
            }

        }

        // Set TopBar Title
        setActionBarTitle(fac.getName());

        return v;
    }

    @Override
    public void onDestroy(){
        ((MainActivity) getActivity()).restoreTitle();
        super.onDestroy();
    }

    //*****************************************
    // Google Maps Api Required Methods
    //*****************************************

    @Override
    public void onMapReady(GoogleMap gMap) {
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

/*    private class GetDetails extends AsyncTask<String, Void, String> {
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
    }*/

}
