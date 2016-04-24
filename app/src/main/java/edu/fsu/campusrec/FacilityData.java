package edu.fsu.campusrec;

/**
 * FacilityData
 * Abstract data container class designed to hold hardcoded, constant data
 */

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public abstract class FacilityData {

    public enum Amenities {
        WIFI,
        RESERVE,
        EQUIPMENT,
        RUN,
        BIKE,
        SWIM,
        ROW,
        FIELD,
    }

    //**************************************
    //  LOCATIONS
    //**************************************

    public static final LatLng LEACH_LATLNG = new LatLng(30.4419465, -84.3018533);
    public static final LatLng REZ_LATLNG = new LatLng(30.4011161, -84.3369554);
    public static final LatLng FMC_LATLNG = new LatLng(30.4417118, -84.299023);
    public static final LatLng RSP_LATLNG = new LatLng(30.4198941, -84.3386871);
    public static final LatLng MCF_LATLNG = new LatLng(30.4374131, -84.2992917);
    public static final LatLng WSC_LATLNG = new LatLng(30.44692326, -84.30364251);

    //**************************************
    //  ADDRESSES
    //**************************************

    public static final String LEACH_ADDRESS = "118 Varsity Way, Tallahassee, FL 32306";
    public static final String REZ_ADDRESS = "3226 Flastacowo Road, Tallahassee, FL 32310";
    public static final String FMC_ADDRESS = "960 Learning Way, Tallahassee, FL 32306";
    public static final String RSP_ADDRESS = "3950 Tyson Road, Tallahassee, 32310";
    public static final String MCF_ADDRESS = "1001 W. St. Augustine Street, Tallahassee, 32306";
    public static final String WSC_ADDRESS = "240 Hull Drive, Tallahassee, 32306";

    //**************************************
    //  PHONE NUMBERS
    //**************************************

    public static final String LEACH_NUMBER = "850.644.0548";
    public static final String REZ_NUMBER = "850.644.6892";
    public static final String FMC_NUMBER = "850.645.0601";
    public static final String RSP_NUMBER = "850.645.7246";

    //**************************************
    //  RAINLINE INFORMATION
    //**************************************

    public static final String RAINLINE_USERNAME = "fsuimrainline";
    public static final String RAINLINE_PHONE = "tel:8506457246";

    //**************************************
    //  SOCIAL MEDIA LINKS
    //**************************************
    public static final Uri CR_FACEBOOK_WEB = Uri.parse("https://www.facebook.com/fsucampusrec");
    public static final Uri CR_FACEBOOK_OLD = Uri.parse("fb://page/fsucampusrec");
    public static final Uri CR_FACEBOOK_NEW = Uri.parse("fb://facewebmodal/f?href=https://www.facebook.com/fsucampusrec");

    public static final Uri CR_TWITTER_WEB = Uri.parse("https://twitter.com/FSUCampusRec");
    public static final Uri CR_TWITTER = Uri.parse("twitter://user?user_id=605880659");

    public static final Uri CR_INSTAGRAM = Uri.parse("https://www.instagram.com/_u/fsucampusrec/");

    public static final Uri CR_YOUTUBE = Uri.parse("https://www.youtube.com/user/FSUCampusRec");

}
