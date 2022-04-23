package controllers;

import conn.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import models.Person;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class AdminViewController {
    private Main main;
    private Stage mainStage;

    @FXML
    public Label balanceLabel;

    @FXML
    private RadioButton allUsersBtn;

    @FXML
    private RadioButton oneUsersBtn;

    private Person currentPerson;

    static float summ;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void showData(Person person){
        balanceLabel.setText(String.valueOf(person.getBalance()));
        currentPerson = person;
    }

    public static float getSumm() {
        return summ;
    }

    public static void setSumm(float summ) {
        AdminViewController.summ = summ;
    }

    public Label getBalanceLabel() {
        return balanceLabel;
    }

    @FXML
    public void clickForExit() {
        mainStage.close();
    }

    @FXML
    private void operationMethod() throws SQLException, IOException {
        if(allUsersBtn.isSelected() == true){
            DataBaseHandler.payAllPerson();
            JOptionPane.showMessageDialog(null, "Кошти перераховані усім користувачам успішно!");
            balanceLabel.setText(String.valueOf(currentPerson.getBalance() - summ));
        }else if(oneUsersBtn.isSelected() == true){
            makePayMethod();
        }
    }

    @FXML
    private void makePayMethod() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/pay_admin_person.fxml"));
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Відправити");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        PayAdminForPersonController controller = loader.getController();
        controller.setCurrentPerson(currentPerson);
        controller.setMain(main);
        controller.setStage(stage);
        controller.setParentController(this);

        stage.show();
    }


}
