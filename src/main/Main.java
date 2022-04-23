package main;

import com.itextpdf.text.DocumentException;
import conn.CardIssuanceForUser;
import conn.DataBaseHandler;
import controllers.*;
import instruments.CreatePDFfilesClass;
import instruments.Validator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import models.OperationItem;
import models.Person;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main extends Application {
    private Stage mainStage;
    private BorderPane mainPane;


    @FXML
    public void initialize() {}

    @Override
    public void start(Stage stage) throws Exception{
        mainStage = stage;
        mainStage.setTitle(" ");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/main_view.fxml"));
        mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        mainStage.setScene(scene);
        //фиксированный размер окна
        mainStage.setResizable(false);

        MainViewController controller = loader.getController();
        controller.setMainStage(mainStage);
        controller.setMain(this);

        switchViewLogin();


        mainStage.show();
    }

    public void switchViewLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/login_view.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);

        LoginViewController controller = loader.getController();
        controller.setMainStage(mainStage);
        controller.setMain(this);
    }

    public void switchViewRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/registration_view.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);

        RegistrationViewController controller = loader.getController();
        controller.setMainStage(mainStage);
        controller.setMain(this);
    }

    public void switchViewAdmin(Person person) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/admin_view.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);

        AdminViewController controller = loader.getController();
        controller.setMainStage(mainStage);
        controller.showData(person);
        controller.setMain(this);
    }

    public void switchViewAccount(Person person) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/account_view.fxml"));
        AnchorPane pane = loader.load();
        mainPane.setCenter(pane);

        AccountViewController controller = loader.getController();
        controller.setStage(mainStage);
        controller.setMain(this);
        controller.showData(person);
    }


    public static void main(String[] args) throws SQLException, FileNotFoundException, DocumentException {

        /*CreatePDFfilesClass dd = new CreatePDFfilesClass();
        ObservableList<OperationItem> incomeList = FXCollections.observableArrayList();
        ObservableList<OperationItem> costsList = FXCollections.observableArrayList();
        int day = 9;
        int month = 10;
        int year = 2000;
        LocalDate date = LocalDate.of(year, month, day);
        Person osoba = new Person("9999", "678", "12"
                , "Vanov", "Ivan", "Ivanovich", date, 0, incomeList , costsList);



        //System.out.println(DataBaseHandler.generateCodeNumber());
        Person second = DataBaseHandler.searchSecondPersonForDocument("333444555");
        try {
            dd.createDocumnetOfTransakcia(osoba, second, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        launch(args);
    }

}
