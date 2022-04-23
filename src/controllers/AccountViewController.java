package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import javafx.scene.control.Label;
import models.OperationItem;
import models.Person;

import java.io.IOException;
import java.sql.Timestamp;

public class AccountViewController {
    @FXML
    public Label nameUserLabel;

    @FXML
    public Label balanceLabel;

    @FXML
    public Label numberCodeLabel;

    @FXML
    private TableView<OperationItem> operationTable;
    @FXML
    private TableColumn<OperationItem, Timestamp> dateOperationColumn;
    @FXML
    private TableColumn<OperationItem, Float> summOperationColumn;
    @FXML
    private TableColumn<OperationItem, String> fromOperationColumn;

    @FXML
    private TableView<OperationItem> operationMyTable;
    @FXML
    private TableColumn<OperationItem, Timestamp> dateObjectColumn;
    @FXML
    private TableColumn<OperationItem, Float> summObjectColumn;
    @FXML
    private TableColumn<OperationItem, String> objectColumn;

    private Main main;
    private Stage mainStage;
    private LoginViewController parentController;
    private Person currentPerson;

    @FXML
    private void initialize(){
        /*dateOperationColumn.setCellValueFactory(
                cellValue->new SimpleStringProperty(String.valueOf(cellValue.getValue().getDate()))
        );
        summOperationColumn.setCellValueFactory(
                cellValue->new SimpleObjectProperty(cellValue.getValue().getSumm())
        );
        fromOperationColumn.setCellValueFactory(
                cellValue->new SimpleStringProperty(cellValue.getValue().getFrom())
        );
        operationTable.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showSubjectInfo(newValue))
        );*/
    }

    public TableView<OperationItem> getOperationTable() {
        return operationTable;
    }

    public TableView<OperationItem> getOperationMyTable() {
        return operationMyTable;
    }

    public Label getBalanceLabel() {
        return balanceLabel;
    }

    public void showData(Person person){
        nameUserLabel.setText(person.getSurname() + " " + person.getName() + " " + person.getFatherName());
        balanceLabel.setText(person.getBalance() + " грн");
        numberCodeLabel.setText(person.getNumberCode());
        showincomeList(person);
        showCostList(person);
        currentPerson = person;
    }

    private void showincomeList(Person newValue) {
        dateOperationColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, java.sql.Timestamp>("date"));
        summOperationColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, Float>("summ"));
        fromOperationColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, String>("pay"));
        operationTable.setItems(newValue.incomeList);
    }
    private void showCostList(Person newValue) {
        dateObjectColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, java.sql.Timestamp>("date"));
        summObjectColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, Float>("summ"));
        objectColumn.setCellValueFactory(new PropertyValueFactory<OperationItem, String>("to"));
        operationMyTable.setItems(newValue.costsList);
    }

    public void setMain(Main main) {
        this.main = main;

        operationTable.refresh();
    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }

    public void setParentController(LoginViewController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void clickForExit(){
        mainStage.close();
    }

    @FXML
     void makePayMethod() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/pay_for_person.fxml"));
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Сплатити");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        PayForPersonController controller = loader.getController();
        controller.setCurrentPerson(currentPerson);
        controller.setMain(main);
        controller.setStage(stage);
        controller.setParentController(this);

        stage.show();
    }

    @FXML
     void payForServiceMethod() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../views/service_list.fxml"));
        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Сплатити");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        PayServiceListController controller = loader.getController();
        controller.setCurrentPerson(currentPerson);
        controller.setMain(main);
        controller.setStage(stage);
        controller.setParentController(this);

        stage.show();
    }

}
