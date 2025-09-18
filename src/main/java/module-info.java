module com.example.speedkeys {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.speedkeys to javafx.fxml;
    exports com.example.speedkeys;
}