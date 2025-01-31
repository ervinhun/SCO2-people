package dk.easv.people;

import dk.easv.people.be.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;


public class MainApp extends Application {
    private ObservableList<Person> personData;
    private BorderPane root;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("root-layer.fxml"));
        root = fxmlLoader.load();

        Scene scene = new Scene(root, 610, 430);

        showPersonOverview();

        stage.setTitle("AddressApp");
        //stage.setAlwaysOnTop(true);
        //stage.getIcons().add(new Image("file:resources/img/smart.png"));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/smart.png"))));
        stage.setScene(scene);
        stage.show();
        //setDummyPersonData();
    }

    public void setDummyPersonData() {
        personData = FXCollections.observableArrayList();

        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    public void addPerson(Person person) {
        personData.add(person);
    }

    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("person-overview.fxml"));
            AnchorPane personOverview = loader.load();

            // Set person overview into the center of root layout.
            root.setCenter(personOverview);

            // Give the controller access to the main app.
            PersonController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Person person, boolean editing) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("person-edit-dialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            if (editing) {
                dialogStage.setTitle("Edit Person");
            }
            else
                dialogStage.setTitle("Add Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (editing)
                controller.setPerson(person, true);
            else
                controller.setPerson(person, false);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch();

    }

    public Window getPrimaryStage() {
        return root.getScene().getWindow();
    }
}