package controllers;

import conn.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import models.OperationItem;
import models.Person;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PayAdminForPersonController {
    private Main main;
    private Stage stage;
    private AdminViewController parentController;
    DataBaseHandler db;

    @FXML
    private TextField SummaTextField;

    @FXML
    private TextField CodeNumberTextField;

    Person currentPerson;


    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentController(AdminViewController parentController) {
        this.parentController = parentController;
    }

    @FXML
     void payByPerson() throws SQLException {
        String numberCode = CodeNumberTextField.getText();
        float sizePay = Float.parseFloat(SummaTextField.getText());
        java.util.Date dt = new java.util.Date();
        Timestamp ts = new Timestamp(dt.getTime());

        if(numberCode == null){
            JOptionPane.showMessageDialog(null, "Введіть номер рахунку!");
        } else{
            if (SummaTextField.getText() == null){
                JOptionPane.showMessageDialog(null, "Введіть суму!");
            }else if(sizePay <= 0){
                JOptionPane.showMessageDialog(null, "Сума не може бути 0!");
            }else{
                DataBaseHandler.saveOperation(currentPerson, numberCode, sizePay);
                currentPerson.getCostsList().add(new OperationItem(currentPerson.getNumberCode(), numberCode, sizePay, ts));
                parentController.getBalanceLabel().setText(String.valueOf(currentPerson.balance - sizePay));
                stage.close();
            }
        }
    }

    @FXML
     void closePay() {
        stage.close();
    }
}
