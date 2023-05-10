package com.mynotepad.mynotepad;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class FindController implements Initializable {

    @FXML
    private TextField txtFind;
    @FXML
    private CheckBox checkboxMatchCase;
    @FXML
    private CheckBox checkboxWrapAround;
    @FXML
    private CheckBox checkboxUp;
    @FXML
    private CheckBox checkboxDown;
    @FXML
    private Button findButton;
    @FXML
    private Button cancelButton;

    private TextArea text;

    int caretPosition = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void findTyped(KeyEvent event) {

    }

    @FXML
    private void checkboxMatchCase(MouseEvent event) {
    }

    @FXML
    private void checkboxWrapAroundClicked(MouseEvent event) {
    }

    @FXML
    private void checkboxUpClicked(MouseEvent event) {
    }

    @FXML
    private void checkboxDownClicked(MouseEvent event) {
    }

    @FXML
    private void findClicked(MouseEvent event) {

//        String temp = text.getText().substring(caretPosition, text.getText().length());
        text.requestFocus();
//        text.positionCaret(temp.indexOf(txtFind.getText()));
        caretPosition = text.getCaretPosition(); //ovo radi
        text.selectNextWord(); // i ovo isto radi

        System.out.println(text.getSelectedText());

    }

    @FXML
    private void cancelClicked(MouseEvent event) {

    }

    public void setText(TextArea text) {
        this.text = text;
    }
}
