package com.mynotepad.mynotepad.mycontrols;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class MyComboBox<T> extends ComboBox {

    private Label label;
    private Slider slider;
    private FontWeight fw;
    private FontPosture fp;
    

    public MyComboBox() {

        List<String> fonts = Font.getFamilies();
        getItems().addAll(fonts);
        setOnAction((t) -> {
            String selectedFont = (String) getSelectionModel().getSelectedItem();
            Platform.runLater(() -> {
                label.setFont(Font.font(selectedFont, fw, fp, slider.getValue()));
                label.setText(selectedFont + " " + (int) slider.getValue());
            });
        });
    }

    public void manageControls(Label label, Slider slider, FontWeight fw, FontPosture fp) {

        this.label = label;
        this.slider = slider;
        this.fw = fw;
        this.fp = fp;
    }
}
