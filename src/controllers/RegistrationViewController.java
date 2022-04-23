package controllers;

import com.itextpdf.text.DocumentException;
import conn.CardIssuanceForUser;
import conn.DataBaseHandler;

import conn.PersonalDataControl;
import controllers.list_enums.AlertRegistration;
import controllers.list_enums.Gender;
import instruments.Validator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import main.Main;
import models.OperationItem;
import models.Person;
import securityblock.AESUtils;
import securityblock.CryptoControl;

import java.io.IOException;
import java.sql.SQLException;

import java.time.LocalDate;

import java.time.Period;


public class RegistrationViewController {

    private Main main;
    private Stage mainStage;
    private DataBaseHandler db;

    @FXML
    private TextField surnameLabel;

    @FXML
    private TextField nameLabel;

    @FXML
    private TextField fathernameLabel;


    @FXML
    private DatePicker datePicker = new DatePicker();

    @FXML
    private TextField emailLabel;

    @FXML
    private TextField telefonNumberLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private ComboBox<String> genderComboBox = new ComboBox<>();


    Validator validator = new Validator();


    Validator.PasswordValidator validatorPassword = new Validator.PasswordValidator();



    PersonalDataControl personalDataControl = new PersonalDataControl();

    @FXML
    public void initialize() {
        ObservableList<String> genderList = FXCollections.observableArrayList();
        genderList.add(Gender.female_translated);
        genderList.add(Gender.male_translated);
        genderComboBox.setItems(genderList);
        genderComboBox.getSelectionModel().select(0);
        //genderComboBox.getSelectionModel().isSelected(0);

        LocalDate date = LocalDate.now();
        datePicker.setValue(date);
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }



    @FXML
     void registrationPersonMethod() throws IOException, SQLException, DocumentException, IllegalAccessException {

        String surname = textFieldDataChecking(surnameLabel.getText());
        String name = textFieldDataChecking(nameLabel.getText());
        String fatherName = textFieldDataChecking(fathernameLabel.getText());

        String email = textFieldDataChecking(emailLabel.getText());
        String phone = textFieldDataChecking(telefonNumberLabel.getText());
        String password = textFieldDataChecking(passwordField.getText());


        LocalDate date = datePicker.getValue();


        ObservableList<OperationItem> incomeList = FXCollections.observableArrayList();
        ObservableList<OperationItem> costsList = FXCollections.observableArrayList();

        String gender = genderComboBox.getValue().equals(Gender.male_translated) ? Gender.MALE : Gender.FEMALE;
        Person person = new Person(db.generateCodeUniqueness(), phone, email, password, surname, name, fatherName,  gender, date,0, incomeList, costsList);

        System.out.println(person);

        //если данные не указаны, в поле обьекта вставляем null. Если в обьекте хоть один null метод isNull блокирует его.
        if (person.isNull()) {
            if (validator.methodValidPhoneNumber(phone) && validator.methodValidEmail(email)
                    && validatorPassword.methodValidPassword(person) && Validator.ValidatorForPersonality.methodCheckPersonalityData(surname) &&
                    Validator.ValidatorForPersonality.methodCheckPersonalityData(name) && Validator.ValidatorForPersonality.methodCheckPersonalityData(fatherName)
                    && !personalDataControl.methodCheckToNameAndSurname(person) && personalDataControl.methodCheckContactData(person)) {

                if (!passwordField.getText().equals(repeatPasswordField.getText())) {
                    methodAlertErrorOnRegistration(AlertRegistration.passwordsNotIdentical);
                } else {

                    //TODO написать функцию которая в бд будет проверять, клиента на идентичность. если такой клиент уже есть - отказ.
                    //TODO кодирование пароля в бд при реестрции, и декодирование при авторизации
                    //Tckb
                    //возвраст клиента
                    if (takeAgeOfClient(person.birthday) < 16) {
                        methodAlertErrorOnRegistration(AlertRegistration.ageOfClientError);
                    } else {
                        //кодируем пароль
                        person.setPassword(AESUtils.encrypt(person.password, CryptoControl.secretKey));
                        db.insertPerson(person);
                        CardIssuanceForUser.insertCardForUser(person);
                        main.switchViewLogin();
                    }
                }
            }
        }else {
            methodAlertErrorOnRegistration(AlertRegistration.allFieldsEmpty);
        }

    }

    //возраст клиента дожен быть старше 16 лет
    private int takeAgeOfClient (LocalDate localDate){
        LocalDate now = LocalDate.now();

        return Period.between(localDate, now).getYears();
    }


    //Alert об ошибке
    private void methodAlertErrorOnRegistration (String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alert");
        alert.setContentText(text);

        alert.showAndWait();

    }


    private String textFieldDataChecking(String value) {
        if(value.isEmpty() || value.trim().isEmpty() || value == null) {
            return null;
        }else return value;
    }


    @FXML
    public void closeRegistration () {
        mainStage.close();
    }
}
