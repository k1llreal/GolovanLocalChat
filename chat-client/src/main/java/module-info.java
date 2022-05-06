module chat.client {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;
    requires io.netty.all;

    opens ru.golovan to javafx.fxml;
    exports ru.golovan;
}