module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.desktop;

    opens org.example to javafx.fxml, javafx.graphics;
    exports org.example;
}