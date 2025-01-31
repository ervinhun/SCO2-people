package dk.easv.people;

import dk.easv.people.be.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PersonEditDialog implements Initializable {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField postalCodeField;
    @FXML private TextField cityField;
    @FXML private TextField birthdayField;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okClicked = false;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPerson(Person person, boolean editing) {
        this.person = person;
        if (editing) {
            firstNameField.setText(person.getFirstName());
            lastNameField.setText(person.getLastName());
            streetField.setText(person.getStreet());
            postalCodeField.setText(Integer.toString(person.getPostalCode()));
            cityField.setText(person.getCity());
            //birthdayField.setText(DateUtil.format(person.getBirthday()));
            birthdayField.setText(person.getBirthday().toString());
            //birthdayField.setPromptText("dd.mm.yyyy");
        }
        else {
            firstNameField.setText("");
            lastNameField.setText("");
            streetField.setText("");
            postalCodeField.setText("");
            cityField.setText("");
            birthdayField.setText("");
        }
        birthdayField.setPromptText("yyyy-mm-dd");
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void okClickedHandle(ActionEvent event) {
        okClicked = true;
        if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(LocalDate.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    public void cancelClickedHandle(ActionEvent event) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage += "No valid street!\n";
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No valid city!\n";
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (false) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
