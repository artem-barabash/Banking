package controllers;

import conn.DataBaseHandler;
import controllers.list_enums.ServiceConstant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import main.Main;
import models.OperationItem;
import models.Person;
import models.Service;

import javax.swing.*;
import java.sql.Timestamp;

public class PayServiceListController {
    private Main main;
    private Stage stage;
    private AccountViewController parentController;
    private Person currentPerson;
    private DataBaseHandler db;

    @FXML
    private RadioButton payStudyBtn;
    @FXML
    private RadioButton payGasBtn;
    @FXML
    private RadioButton payWaterBtn;
    @FXML
    private RadioButton payElectricBtn;
    @FXML
    private RadioButton payInternetBtn;

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

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
    public void payByService() {
        java.util.Date dt = new java.util.Date();
        Timestamp ts = new Timestamp(dt.getTime());

        if(payStudyBtn.isSelected()){
            payInSystem(ServiceConstant.education, ts);
        }else if(payGasBtn.isSelected()){
            payInSystem(ServiceConstant.heating, ts);
        }else if(payWaterBtn.isSelected()){
            payInSystem(ServiceConstant.watersupply, ts);
        }else if(payElectricBtn.isSelected()){
            payInSystem(ServiceConstant.electrscity, ts);
        }else if(payInternetBtn.isSelected()){
            payInSystem(ServiceConstant.internet, ts);
        }else{
            JOptionPane.showMessageDialog(null, "Оберіть послугу за яку будете платити!");
        }
    }

    private  void payInSystem(Service service, Timestamp ts){
        if(currentPerson.getBalance() < service.getSumm()){
            JOptionPane.showMessageDialog(null, "Не достатньо коштів!");
        }else {
            int confirm = JOptionPane.showConfirmDialog(null, "Ви підтверджуєте здійснення операції?", "Підтвердження", JOptionPane.YES_NO_CANCEL_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                db.saveOperationOfService(currentPerson, service.getName(), service.getSumm());
                currentPerson.getCostsList().add(new OperationItem(currentPerson.getNumberCode(), service.getName(), service.getSumm(), ts));
                parentController.getBalanceLabel().setText(String.valueOf(currentPerson.balance - service.getSumm()));
                parentController.getOperationMyTable().refresh();
                stage.close();
            }else{
             stage.close();
            }
        }
    }


    @FXML
    void closeOperation() {
        stage.close();
    }
}
