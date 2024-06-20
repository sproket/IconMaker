module org.example.iconmaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.desktop;

    opens org.example.iconmaker to javafx.fxml;
    opens org.example to javafx.graphics;
    exports org.example.iconmaker;
}