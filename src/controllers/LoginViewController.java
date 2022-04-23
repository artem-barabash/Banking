package controllers;

import conn.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import models.OperationItem;
import models.Person;
import securityblock.AESUtils;
import securityblock.CryptoControl;

import java.io.IOException;

public class LoginViewController {
    private Main main;
    private Stage mainStage;
    Person person;
    @FXML
    public TextField LoginTextField;
    @FXML
    public PasswordField PasswordTextField;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @FXML
     void registrationClick() throws IOException {
        main.switchViewRegistration();
    }

    @FXML
     void exitPerson() throws IOException {
        DataBaseHandler db = new DataBaseHandler();
        String phone = LoginTextField.getText();
        String password = PasswordTextField.getText();

        //ищем по кодированому паролю
        String encryptPassword = AESUtils.encrypt(password, CryptoControl.secretKey);

        Person osoba = DataBaseHandler.exitUser(phone, encryptPassword);
        try {
            if(osoba != null) {
                if(osoba.getPhone().equals("admin")){
                    main.switchViewAdmin(osoba);
                } else{
                    osoba.incomeList = DataBaseHandler.incomeListOfPerson(osoba);
                    osoba.costsList = DataBaseHandler.costsListOfPerson(osoba);
                    main.switchViewAccount(osoba);
                }

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    @FXML
     void clickClose() {
        mainStage.close();
    }

}
