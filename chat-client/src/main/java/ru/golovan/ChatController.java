package ru.golovan;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private Network network;
    @FXML
    TextField messageField;

    @FXML
    TextArea mainArea;

    //при запуске окна, выполняется метод initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network((args) -> {
            mainArea.appendText((String)args[0]);
        });
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        //отправляем сообщение в сеть
        network.sendMessage(messageField.getText());

        //отчищаем поле messageField так как отправили сообщение
        messageField.clear();

        //переставляем фокус обратно, чтобы пользователь дальше смог писать
        messageField.requestFocus();
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }
}