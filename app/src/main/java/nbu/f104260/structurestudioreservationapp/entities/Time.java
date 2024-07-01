package nbu.f104260.structurestudioreservationapp.entities;

import java.io.Serializable;

public class Time implements Serializable {

    private String time;

    public Time(String time) {
        this.time = time;
    }

    public Time() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
