package controllers;

import conn.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import models.OperationItem;
import models.Person;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PayForPersonController{
    private Main main;
    private Stage stage;
    private AccountViewController parentController;

    DataBaseHandler db;
    Person currentPerson;

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }


    @FXML
    private TextField SummaTextField;

    @FXML
    private TextField CodeNumberTextField;



    public void setMain(Main main) {
        this.main = main;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setParentController(AccountViewController parentController) {
        this.parentController = parentController;
    }



    @FXML
     void payByPerson() throws SQLException, IllegalArgumentException {
        String numberCode = CodeNumberTextField.getText();
        float sizePay = Float.parseFloat(SummaTextField.getText());
        java.util.Date dt = new java.util.Date();
        Timestamp ts = new Timestamp(dt.getTime());

        if(numberCode == null){
            JOptionPane.showMessageDialog(null, "Введіть номер рахунку!");
        }else{
            if(numberCode.equals(currentPerson.getNumberCode())){
                JOptionPane.showMessageDialog(null, "Ви ввели свій номер рахунку!");
            }else if(!DataBaseHandler.allPersonNumbers(numberCode)){
                JOptionPane.showMessageDialog(null, "Ви ввели не правильний номер рахунку!");
            }else{
                if (SummaTextField.getText() == null){
                    JOptionPane.showMessageDialog(null, "Введіть суму!");
                }else if(sizePay <= 0){
                    JOptionPane.showMessageDialog(null, "Сума не може бути 0!");
                }else if(currentPerson.getBalance() < sizePay){
                    JOptionPane.showMessageDialog(null, "Не достатньо коштів!");
                }else{
                    int confirm = JOptionPane.showConfirmDialog(null, "Ви підтверджуєте здійснення операції?", "Підтвердження", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(confirm == JOptionPane.YES_OPTION){
                        DataBaseHandler.saveOperation(currentPerson, numberCode, sizePay);
                        currentPerson.getCostsList().add(new OperationItem(currentPerson.getNumberCode(), numberCode, sizePay, ts));
                        parentController.getBalanceLabel().setText(String.valueOf(currentPerson.balance - sizePay));
                        parentController.getOperationMyTable().refresh();
                        stage.close();
                    }else{
                        stage.close();
                    }

                }
            }
        }
    }

    @FXML
     void closePay() {

        stage.close();
    }
}


