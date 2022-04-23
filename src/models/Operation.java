package models;

public class Operation {
    public void sendToCount(Person person1, Person person2, double money) {
        if(person1.balance > money) {
            person1.balance -= money;
            person2.balance += money;
        }else {
            System.out.println("Недостатньо коштів!");
        }
    }

    public void payByPhone(Person person, String tariff, double money) {
        if(person.balance > money) {
            person.balance += money;
        }else {
            System.out.println("Недостатньо коштів!");
        }
    }
}
