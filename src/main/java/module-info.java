module dk.easv.people {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.people to javafx.fxml;
    exports dk.easv.people;
}