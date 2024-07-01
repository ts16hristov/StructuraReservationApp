package nbu.f104260.structurestudioreservationapp.entities;


import java.io.Serializable;

public class Barber  implements Serializable {

    private long id;

    private String name;

    private String position;

    public Barber(long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Barber() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
