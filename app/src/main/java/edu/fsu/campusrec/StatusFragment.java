package edu.fsu.campusrec;


import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class StatusFragment extends Fragment {
    private final static String LEACH_FMC_URL = "http://campusrec.fsu.edu/fitness/leach-fmc";
    private Facility fac;

    private SlidingUpPanelLayout slider;
    public StatusFragment() { }
    private View mainContainer;

    public static StatusFragment newInstance(Facility fac){
        StatusFragment sFrag = new StatusFragment();
        sFrag.setBuilding(fac);
        return sFrag;
    }

    private void setBuilding(@NonNull Facility fac){
        this.fac = fac;
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

        ImageView header = (ImageView) v.findViewById(R.id.header_image);
        TextView headerName = (TextView) v.findViewById(R.id.fac_name);
        TextView status = (TextView) v.findViewById(R.id.status_detail);
        TextView day;
        TextView label;
        TextView details = (TextView) v.findViewById(R.id.details);

        if(activeFac.getPhoto() != -1) {
            header.setVisibility(View.VISIBLE);
            header.setImageResource(activeFac.getPhoto());
            header.setContentDescription(activeFac.getName());
        }
        else {
            header.setVisibility(View.GONE);
        }
        headerName.setText(activeFac.getName());
        status.setText(activeFac.getStatus());

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

        setActionBarTitle(activeFac.getName());
        setDetailSubtitle();

        return v;
    }

    private void setCurrentDayText(TextView label, TextView hours){
        label.setTextSize(15);
        label.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        hours.setTextSize(15);
        hours.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        hours.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @SuppressWarnings("ConstantConditions")
    private void setActionBarTitle(String title){
        if(((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @SuppressWarnings("ConstantConditions")
    private void setDetailSubtitle(){
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Details");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        ((MainActivity) getActivity()).restoreTitle();
        super.onDestroy();
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
