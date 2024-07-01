package nbu.f104260.structurestudioreservationapp.entities;

import java.io.Serializable;

public class Appointment implements Serializable {

    private Long barberId;

    private String service;

    private String date;

    private String time;

    private String clientEmail;

    private int price;

    public Appointment(Long barberId, String service, String date, String time, String clientEmail, int price) {
        this.barberId = barberId;
        this.service = service;
        this.date = date;
        this.time = time;
        this.clientEmail = clientEmail;
        this.price = price;
    }

    public Appointment() {
    }

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
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
