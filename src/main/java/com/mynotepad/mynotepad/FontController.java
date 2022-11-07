package com.mynotepad.mynotepad;

import com.mynotepad.mynotepad.mycontrols.MyComboBox;
import com.mynotepad.mynotepad.mycontrols.MySlider;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class FontController implements Initializable {

    @FXML
    private Label fontLabel;
    @FXML
    private MySlider slider;
    @FXML
    private MyComboBox comboBox; //u module info exportat package mycontrols i u fxmlu promjeniti tip iz combobox u mycombobox
    @FXML
    private CheckBox checkBoxBold;
    @FXML
    private CheckBox checkBoxItalic;

    FontPosture fp;
    FontWeight fw;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialize...");
        fp = FontPosture.REGULAR;
        fw = FontWeight.NORMAL;
        
        comboBox.manageControls(fontLabel, slider, fw, fp);
        slider.manageControls(fontLabel, fp, fw);
        
    }

    @FXML
    private void checkBoxItalicClicked(ActionEvent event) {
        fp = ((CheckBox) event.getSource()).isSelected() ? FontPosture.ITALIC : FontPosture.REGULAR;
//        CheckBox temp = (CheckBox) event.getSource();
//
//        if (temp.isSelected()) {
//            fp = FontPosture.ITALIC;
//        } else {
//            fp = FontPosture.REGULAR;
//        }
        Platform.runLater(() -> fontLabel.setFont(Font.font(fontLabel.getFont().getFamily(), fw, fp, fontLabel.getFont().getSize())));

    }

    @FXML
    private void checkBoxBoldClicked(ActionEvent event) {

        fw = ((CheckBox) event.getSource()).isSelected() ? FontWeight.BOLD : FontWeight.NORMAL;
//        CheckBox temp = (CheckBox) event.getSource();
//
//        if (temp.isSelected()) {
//            fw = FontWeight.BOLD;
//        } else {
//            fw = FontWeight.NORMAL;
//        }
        Platform.runLater(() -> fontLabel.setFont(Font.font(fontLabel.getFont().getFamily(), fw, fp, fontLabel.getFont().getSize())));

    }

    public void setFont(Font font) {
        System.out.println("Metoda setFont...");
        Platform.runLater(() -> {
            fontLabel.setFont(font);
            fontLabel.setText(font.getFamily() + " " + (int) font.getSize());
            slider.setValue(font.getSize());
            comboBox.getSelectionModel().select(font.getFamily());
        });

    }

    public Font getFont() {
        return fontLabel.getFont();
    }

}
