package edu.fsu.campusrec;

import java.util.ArrayList;
import java.util.HashMap;

public class Facility {
    private final String STATUS_OPEN = "OPEN";
    private final String STATUS_CLOSED = "CLOSED";

    private String name;
    private int photo;
    private ArrayList<opHours> hoursOfOperation;
    private boolean open;
    private Building bldg;

    public enum Building {
        LEACH, REZ, FMC, RSP, MCF, WSC
    }

    public Facility(Building bldg, int resPhoto, ArrayList<opHours> hOO, boolean open) {
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
        this.photo = resPhoto;
        if (hOO.size() != 7)
            throw new IllegalArgumentException("Not enough days!");
        this.hoursOfOperation = hOO;
        this.open = open;
    }

    public Building getBldg(){ return this.bldg;}

    public String getName(){
        return this.name;
    }

    public int getPhoto(){
        return this.photo;
    }

    public String getStatus(){
        if(this.open)
            return STATUS_OPEN;
        else
            return STATUS_CLOSED;
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
