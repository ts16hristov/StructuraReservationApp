package nbu.f104260.structurestudioreservationapp.entities;

import java.io.Serializable;

public class AppointmentHistory implements Serializable {
    private long barberId;
    private String barberName;
    private String service;

    private String date;

    private String time;

    private String clientEmail;

    private int price;

    public AppointmentHistory(long barberId, String barberName, String service, String date, String time, String clientEmail, int price) {
        this.barberId = barberId;
        this.barberName = barberName;
        this.service = service;
        this.date = date;
        this.time = time;
        this.clientEmail = clientEmail;
        this.price = price;
    }

    public AppointmentHistory() {

    }

    public long getBarberId() {
        return barberId;
    }

    public void setBarberId(long barberId) {
        this.barberId = barberId;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
