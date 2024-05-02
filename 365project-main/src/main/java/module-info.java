module com.example.csc365project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires mysql.connector.j;

    opens com.example.csc365project to javafx.fxml;
    exports com.example.csc365project;
}