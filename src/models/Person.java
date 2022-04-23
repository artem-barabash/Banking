package models;

import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Person {
    public String numberCode;
    public String phone;
    public String email;
    public String password;
    public String surname;
    public String name;
    public String fatherName;
    public String gender;
    public LocalDate birthday;
    public float balance;
    public ObservableList<OperationItem> incomeList;
    public ObservableList<OperationItem> costsList;


    public Person(String numberCode, String phone, String email, String password,
                  String surname, String name, String fatherName, String gender, LocalDate birthday,
                  float balance, ObservableList<OperationItem> incomeList, ObservableList<OperationItem> costsList) {
        this.numberCode = numberCode;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.fatherName = fatherName;
        this.gender = gender;
        this.birthday = birthday;
        this.balance = balance;
        this.incomeList = incomeList;
        this.costsList = costsList;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public ObservableList<OperationItem> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(ObservableList<OperationItem> incomeList) {
        this.incomeList = incomeList;
    }

    public ObservableList<OperationItem> getCostsList() {
        return costsList;
    }

    public void setCostsList(ObservableList<OperationItem> costsList) {
        this.costsList = costsList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "numberCode='" + numberCode + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", balance=" + balance +
                ", incomeList=" + incomeList +
                ", costsList=" + costsList +
                '}';
    }

    public boolean isNull() {
        Field fields[] = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                Object value = f.get(this);
                if (value == null) {
                    return false;
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return true;

    }
}

