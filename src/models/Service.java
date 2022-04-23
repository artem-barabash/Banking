package models;

public class Service {
    public String name;
    public float summ;

    public Service(String name, float summ) {
        this.name = name;
        this.summ = summ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSumm() {
        return summ;
    }

    public void setSumm(float summ) {
        this.summ = summ;
    }
}
