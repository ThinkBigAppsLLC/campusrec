package edu.fsu.campusrec;

/**
 * Facility
 * Data structure class designed to represent a facility
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public class Facility {
    private final String STATUS_OPEN = "OPEN";
    private final String STATUS_CLOSED = "CLOSED";

    private String name;
    private ArrayList<opHours> hoursOfOperation;
    private EnumSet<FacilityData.Amenities> amenities;
    private boolean open;
    private Building bldg;
    private LatLng loc;
    private String address;
    private String mainPhone;

    public enum Building {
        LEACH, REZ, FMC, RSP, MCF, WSC
    }

    //**************************************
    //  CONSTRUCTORS
    //**************************************
    public Facility(
            Building bldg,
            ArrayList<opHours> hOO,
            boolean open,
            LatLng loc,
            String adr,
            String phoneNum,
            EnumSet<FacilityData.Amenities> amen
    ) {
        switch (bldg) {
            case LEACH:
                this.name = "Leach Recreation Center";
                break;
            case REZ:
                this.name = "FSU Reservation";
                break;
            case FMC:
                this.name = "Fitness & Movement Clinic";
                break;
            case RSP:
                this.name = "Rec SportsPlex";
                break;
            case MCF:
                this.name = "Main Campus Fields";
                break;
            case WSC:
                this.name = "Westside Courts";
                break;
        }
        this.bldg = bldg;
        if (hOO.size() != 7)
            throw new IllegalArgumentException("Not enough days!");
        this.hoursOfOperation = hOO;
        this.open = open;
        this.loc = loc;
        this.address = adr;
        this.mainPhone = phoneNum;
        this.amenities = amen;
    }

    //**************************************
    //  GETTERS
    //**************************************

    public LatLng getLoc(){ return this.loc; }

    public Building getBldg(){ return this.bldg;}

    public String getName(){
        return this.name;
    }

    public String getStatus(){
        if(this.open)
            return STATUS_OPEN;
        else
            return STATUS_CLOSED;
    }

    public String getAddress(){
        return this.address;
    }

    public String getNumber(){
        return this.mainPhone;
    }

    public HashMap<String, String> getHours(){
        HashMap<String, String> opHours = new HashMap<>();
        opHours.put("sun", this.hoursOfOperation.get(0).toString());
        opHours.put("mon", this.hoursOfOperation.get(1).toString());
        opHours.put("tues", this.hoursOfOperation.get(2).toString());
        opHours.put("wed", this.hoursOfOperation.get(3).toString());
        opHours.put("thurs", this.hoursOfOperation.get(4).toString());
        opHours.put("fri", this.hoursOfOperation.get(5).toString());
        opHours.put("sat", this.hoursOfOperation.get(6).toString());
        return opHours;
    }

    public EnumSet<FacilityData.Amenities> getAmenities () {
        return this.amenities;
    }

    //**************************************
    //  INNER CLASSES
    //**************************************

    public static class opHours {
        private Hours open;
        private Hours close;
        private Special spec;
        protected enum Special {
            ALLDAY, EVENTS, NONE
        }

        public opHours (Hours open, Hours close){
            this.open = open;
            this.close = close;
            this.spec = Special.NONE;
        }

        public opHours (Hours close){
            this.close = close;
            this.spec = Special.NONE;
        }

        public opHours (Special spec){
            this.spec = spec;
        }

        public String toString(){
            switch(this.spec){
                case EVENTS:
                    return "Special Events Only";
                case ALLDAY:
                    return "24 Hours";
                case NONE:
                    if(open == null)
                        return "until " + this.close.toString();
                    else
                        return this.open.toString() + " - " + this.close.toString();
            }
            return null;
        }


        public static class Hours {
            private int hours;
            private int minutes;
            private AMPM ampm;
            protected enum AMPM {
                AM, PM
            }

            public Hours (int hours, int min, AMPM ampm){
                if (hours > 12 || hours < 0)
                    throw new IllegalArgumentException("Hours can only be in the range (0-12)!");
                this.hours = hours;
                if (min > 59 || min < 0)
                    throw new IllegalArgumentException("Minutes can only be in the range (0-59)!");
                this.minutes = min;
                this.ampm = ampm;
            }

            public String toString(){
                String timeHalf;
                if (this.ampm == AMPM.AM)
                    timeHalf = "a";
                else
                    timeHalf = "p";

                return String.format("%02d.%02d%s", this.hours, this.minutes, timeHalf);
            }
        }
    }
}
