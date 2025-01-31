package dk.easv.people;

import dk.easv.people.be.Person;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PersonController implements Initializable {
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableView<Person> personTable;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label streetLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label cityLabel;
    @FXML private Label birthdayLabel;
    @FXML private GridPane personGrid;
    private MainApp mainApp;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        putDataToTable();
        showPersonDetails(null);
    }

    private void putDataToTable() {
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.setDummyPersonData();
        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());

    }

    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(person.getBirthday().toString());
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }


    //Handlers
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert igen = new Alert(Alert.AlertType.CONFIRMATION);
            igen.setTitle("Confirmation");
            igen.setHeaderText(null);
            igen.setContentText("Are you sure you want to delete this person?");
            ButtonType yesButton = new ButtonType("Yes");
            igen.getButtonTypes().setAll(yesButton, ButtonType.NO);

            Optional<ButtonType> result = igen.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                personTable.getItems().remove(selectedIndex);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("You must select a person to delete");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleAddPerson() {
        Person tempPerson = new Person("a", "null");
        System.out.println(mainApp.getPersonData().size() + " - 1");
        //mainApp.getPersonData().add(tempPerson);
        mainApp.addPerson(tempPerson);
        System.out.println(mainApp.getPersonData().size() + " - 2");

        personTable.getSelectionModel().select(tempPerson);
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson, false);
        if (okClicked) {
            //mainApp.getPersonData().add(tempPerson);
            Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
            showPersonDetails(selectedPerson);
            System.out.println(mainApp.getPersonData().size() + " - 3");
        }
    }

    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson, true);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }
}
