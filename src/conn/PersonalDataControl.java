package conn;

import conn.data_for_connect.ConnectDB;
import controllers.list_enums.AlertRegistration;
import controllers.list_enums.Column;
import controllers.list_enums.Gender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.OperationItem;
import models.Person;

import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class PersonalDataControl {
    public PersonalDataControl(){}

    //Проверяем пользователя в базе по ФИО и дате рождения
    public  boolean methodCheckToNameAndSurname(Person person) throws SQLException {
        int count = 0;
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE surname = '" + person.surname + "' AND name = '" + person.name + "'" +
                " AND fathername = '" + person.fatherName + "' AND birthday = '" + Date.valueOf(person.birthday) + "'");
        while (rs.next()) {
            count = rs.getRow();
        }
        rs.close();

        boolean presence = count >= 1;
        if(presence) methodAlertErrorOnRegistration(AlertRegistration.personAlreadyInBase);

        return presence;
    }

    //Проверка email, телефона
    public boolean methodCheckContactData(Person person) throws SQLException {
        return isBusyAnotherPerson(Column.emailColumn, person.email) == 0 && isBusyAnotherPerson(Column.phoneColumn, person.phone) == 0;
    }
    //проверка в базе параметра(столбца) и конкретных данных в нем
    private int isBusyAnotherPerson(String column, String parameter) throws SQLException {
        int count = 0;
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE " + column + " = '" + parameter +"'");
        while (rs.next()) {
            count = rs.getRow();
        }
        rs.close();

        if(count >=1){
            if(column.equals(Column.phoneColumn)){
                methodAlertErrorOnRegistration(AlertRegistration.personAlreadyHavePhone);
            } else if(column.equals(Column.emailColumn)){
                methodAlertErrorOnRegistration(AlertRegistration.personAlreadyHaveEmail);
            }
        }
        System.out.println(column + " - " + count);

        return count;
    }


    //Alert об ошибке
    private  void methodAlertErrorOnRegistration(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setContentText(text);

        alert.showAndWait();
    }

}
