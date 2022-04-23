package models;

import java.sql.Timestamp;
import java.time.LocalDate;

public class OperationItem {
    public String pay;
    public String to;
    public float summ;
    public java.sql.Timestamp date;

    public OperationItem(String pay, String to, float summ, Timestamp date) {
        this.pay = pay;
        this.to = to;
        this.summ = summ;
        this.date = date;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getSumm() {
        return summ;
    }

    public void setSumm(float summ) {
        this.summ = summ;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OperationItem{" +
                "pay='" + pay + '\'' +
                ", to='" + to + '\'' +
                ", summ=" + summ +
                ", date=" + date +
                '}';
    }
}
