package edu.fsu.campusrec;

import android.widget.GridLayout;

import java.util.ArrayList;

public class Facility {
    private final String STATUS_OPEN = "OPEN";
    private final String STATUS_CLOSED = "CLOSED";

    private String name;
    private int photo;
    private ArrayList<opHours> hoursOfOperation;
    private boolean open;

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
        this.photo = resPhoto;
        if (hOO.size() != 7)
            throw new IllegalArgumentException("Not enough days!");
        this.hoursOfOperation = hOO;
        this.open = open;
    }

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

    public String getHours(){
        String ret = "";
        ret += "S | ";
        ret += hoursOfOperation.get(0).toString();
        ret += "M | ";
        ret += hoursOfOperation.get(1).toString();
        ret += "T | ";
        ret += hoursOfOperation.get(2).toString();
        ret += "W | ";
        ret += hoursOfOperation.get(3).toString();
        ret += "T | ";
        ret += hoursOfOperation.get(4).toString();
        ret += "F | ";
        ret += hoursOfOperation.get(5).toString();
        ret += "S | ";
        ret += hoursOfOperation.get(6).toString();
        return ret;
    }
    public static class opHours {
        private Hours open;
        private Hours close;
        private Special spec;
        protected enum Special {
            ALLDAY, EVENTS
        }

        public opHours (Hours open, Hours close){
            this.open = open;
            this.close = close;
        }

        public opHours (Hours close){
            this.close = close;
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
                default:
                    if(open == null)
                        return "until " + this.close.toString();
                    else
                        return this.open.toString() + " - " + this.close.toString();
            }

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

                return this.hours + "." + this.minutes + timeHalf;
            }
        }
    }
}
