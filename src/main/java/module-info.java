module com.mynotepad.mynotepad {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires AnimateFX;
    

    opens com.mynotepad.mynotepad to javafx.fxml;
    exports com.mynotepad.mynotepad;
    exports com.mynotepad.mynotepad.mycontrols;
}
