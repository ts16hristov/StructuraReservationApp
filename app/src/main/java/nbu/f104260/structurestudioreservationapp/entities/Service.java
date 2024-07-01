package nbu.f104260.structurestudioreservationapp.entities;

import java.io.Serializable;

public class Service implements Serializable {
    private String name;

    private int price;

    public Service(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Service() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
