
package com.mynotepad.mynotepad.mycontrols;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class MySlider extends Slider{
    
    
    private FontPosture fp;
    private FontWeight fw;
    private Label label;
    
    public MySlider(){
        
        valueProperty().addListener((ObservableValue<? extends Number> ov, Number t, Number t1) -> {
            Platform.runLater(() -> {
                
                label.setFont(Font.font(label.getFont().getFamily(), fw, fp, t1.doubleValue()));
                label.setText(label.getFont().getFamily() + " " + t1.intValue());
            });
        });
    }
    
    public void manageControls(Label label, FontPosture fp, FontWeight fw){
        
        this.fp = fp;
        this.fw = fw;
        this.label = label;
        
    }

}
