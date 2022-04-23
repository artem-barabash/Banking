package conn;

import com.itextpdf.text.DocumentException;
import conn.data_for_connect.ConnectDB;
import controllers.AdminViewController;
import instruments.CreatePDFfilesClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.OperationItem;
import models.Person;
import securityblock.AESUtils;
import securityblock.CryptoControl;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataBaseHandler {
    AdminViewController updateBalance;

    public static void insertPerson(Person person){
        try {
            String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql);
            pstmt.setString(1, person.getNumberCode());
            pstmt.setString(2, person.getSurname());
            pstmt.setString(3, person.getName());
            pstmt.setString(4, person.getFatherName());
            pstmt.setString(5, person.getPhone());
            pstmt.setString(6, person.getPassword());
            pstmt.setDate(7, Date.valueOf(person.getBirthday()));
            pstmt.setFloat(8, person.getBalance());
            pstmt.setString(9, person.getEmail());
            pstmt.setString(10, person.getGender());
            pstmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public static Person exitUser(String phone, String password) throws IOException {
        try {
            if(phone.length() <= 0 || password.length() <= 0) {
                JOptionPane.showMessageDialog(null, "Ви не ввели логін або пароль!");
            }else {
                int count = 0;
                Statement stmt = ConnectDB.getConnection().createStatement();

                ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE phone = '" + phone + "' AND " +
                        "password = '" +  password + "'");

                while (rs.next()) {
                    count = rs.getRow();
                    ObservableList<OperationItem> incomeList = FXCollections.observableArrayList();
                    ObservableList<OperationItem> costsList = FXCollections.observableArrayList();
                    LocalDate date = rs.getObject("birthday", LocalDate.class);
                    Person osoba = new Person(rs.getString("number"), rs.getString("phone"), rs.getString("email"), rs.getString("password")
                            , rs.getString("surname"), rs.getString("name"), rs.getString("fathername"),rs.getString("gender"), date, rs.getFloat("balance"), incomeList , costsList);


                    return osoba;
                }
                if(count == 0) {
                    JOptionPane.showMessageDialog(null, "Не вірний номер телефону або пароль!");
                }

                rs.close();
            }
        }
        catch (Exception e) {

            System.out.println(e);
        }
        return null;

    }

    public static String generateCodeUniqueness() throws SQLException {
        String current = null;
        ArrayList <String> currentArray = checkForCode();

            for (String num : currentArray){
                String code = generateCodeNumber();

                if(!code.equals(num)){
                    current = code;

                }
            }

        return current;
    }

    public static String generateCodeNumber() {
        String code = "";
        int[] array = new int[9];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) Math.round((Math.random() * 9) - 0);
            code += array[i];
        }
        return code;
    }


    public static ArrayList <String> checkForCode() throws SQLException {
        ArrayList <String> array = new ArrayList<>();
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            String current = rs.getString("number");
            array.add(current);
        }
        return  array;
    }

    public static ObservableList<OperationItem> incomeListOfPerson(Person person) throws SQLException {
        ObservableList<OperationItem> incomeList = FXCollections.observableArrayList();
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM operations WHERE send = '" + person.getNumberCode() + "'");
        while(rs.next()){
            OperationItem item = new OperationItem(rs.getString("pay"), rs.getString("send"), rs.getFloat("summ"), rs.getTimestamp("date"));
            incomeList.add(item);
        }
        return incomeList;
    }

    public static ObservableList<OperationItem> costsListOfPerson(Person person) throws SQLException {
        ObservableList<OperationItem> costsList = FXCollections.observableArrayList();
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM operations WHERE pay = '" + person.getNumberCode() + "'");
        while(rs.next()){
            OperationItem item = new OperationItem(rs.getString("pay"), rs.getString("send"), rs.getFloat("summ"), rs.getTimestamp("date"));
            costsList.add(item);
        }
        return costsList;
    }

    public static void payAllPerson() throws SQLException {
        Statement stmt = ConnectDB.getConnection().createStatement();
        String sql = "UPDATE users SET balance = balance + 1000";
        searchPersonsForOperation();
        stmt.execute(sql);
    }

    private static void searchPersonsForOperation() throws SQLException {
        ArrayList<String> numberPersonList = new ArrayList<>();
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        while(rs.next()){
            String item = rs.getString("number");
            numberPersonList.add(item);
        }
        numberPersonList.remove("admin");
        insertOpereationAllPersons(numberPersonList);
    }

    private static void insertOpereationAllPersons(ArrayList<String> numberPersonList) throws SQLException {
        String sql = "INSERT INTO operations VALUES (?, ?, ?, ?)";
        java.util.Date dt = new java.util.Date();
        Timestamp ts = new Timestamp(dt.getTime());
        int count = 0;

        for(String item : numberPersonList){
            PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql);
            pstmt.setString(1, "admin");
            pstmt.setString(2, item);
            pstmt.setFloat(3, 10);
            pstmt.setTimestamp(4, ts);
            count++;
            pstmt.executeUpdate();
        }

        transakciaRemoveBalance("admin", count * 1000 );
        AdminViewController.setSumm(count * 1000);
    }


    public static void saveOperation(Person person1, String person2Code, float summ){
        try {
            String sql = "INSERT INTO operations VALUES (?, ?, ?, ?)";
            CreatePDFfilesClass pdf = new CreatePDFfilesClass();

            Thread.sleep(1000);
            transakciaRemoveBalance(person1.getNumberCode(), summ);

            Person second = searchSecondPersonForDocument(person2Code);
            pdf.createDocumentOfTransakcia(person1, second, summ);

            Thread.sleep(1000);
            transakciaAddBalance(person2Code, summ);

            java.util.Date dt = new java.util.Date();
            Timestamp ts = new Timestamp(dt.getTime());

            PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql);
            pstmt.setString(1, person1.getNumberCode());
            pstmt.setString(2, person2Code);
            pstmt.setFloat(3, summ);
            pstmt.setTimestamp(4, ts);

            pstmt.executeUpdate();
        }catch (SQLException | InterruptedException | IOException | DocumentException e) {
            e.printStackTrace();
            return;
        }
    }

    public static Person searchSecondPersonForDocument(String codeNumber) throws SQLException {
        Statement stmt = ConnectDB.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE number = '" + codeNumber + "'");
        Person personSecond = null;
        while(rs.next()){
            ObservableList<OperationItem> incomeList = FXCollections.observableArrayList();
            ObservableList<OperationItem> costsList = FXCollections.observableArrayList();
            LocalDate date = rs.getObject("birthday", LocalDate.class);

            personSecond = new Person(rs.getString("number"), rs.getString("phone"), rs.getString("email"), rs.getString("password")
                    , rs.getString("surname"), rs.getString("name"), rs.getString("fathername"),rs.getString("gender"), date, rs.getFloat("balance"), incomeList , costsList);
        }
        return personSecond;
    }


    public static void transakciaAddBalance(String numberCode, float summ) throws SQLException {
        Statement stmt = ConnectDB.getConnection().createStatement();
        String sql = "UPDATE users SET balance = balance +" + summ  + "  WHERE number = '" + numberCode + "'";
        stmt.execute(sql);
    }

    public static void transakciaRemoveBalance(String numberCode, float summ) throws SQLException {
        Statement stmt = ConnectDB.getConnection().createStatement();
        String sql = "UPDATE users SET balance = balance -" + summ  + "  WHERE number = '" + numberCode + "'";
        stmt.execute(sql);
    }

    public static void saveOperationOfService(Person person, String nameService, float summ){
        try {
            String sql = "INSERT INTO operations VALUES (?, ?, ?, ?)";
            CreatePDFfilesClass pdf = new CreatePDFfilesClass();

            Thread.sleep(1000);
            transakciaRemoveBalance(person.getNumberCode(), summ);

            Thread.sleep(1000);
            pdf.createDocumnetOfServiceTransakcia(person, nameService, summ);

            java.util.Date dt = new java.util.Date();
            Timestamp ts = new Timestamp(dt.getTime());

            PreparedStatement pstmt = ConnectDB.getConnection().prepareStatement(sql);
            pstmt.setString(1, person.getNumberCode());
            pstmt.setString(2, nameService);
            pstmt.setFloat(3, summ);
            pstmt.setTimestamp(4, ts);

            pstmt.executeUpdate();
        }catch (SQLException | InterruptedException | IOException | DocumentException e) {
            e.printStackTrace();
            return;
        }
    }

    public  static boolean allPersonNumbers(String numberCode) throws SQLException {
        ArrayList<String> list = checkForCode();
        boolean isNumber = false;
        for(String current : list){
            if(numberCode.equals(current)){
                isNumber = true;
            }
        }
        return isNumber;
    }
}
