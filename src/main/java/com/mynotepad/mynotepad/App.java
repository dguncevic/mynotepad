package com.mynotepad.mynotepad;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import animatefx.animation.*;
import javafx.stage.WindowEvent;

public class App extends Application {

    static Stage stage;
    static Scene scene;
    static AnchorPane anchor;
    NotepadController nc;

    public static void main(String[] args) {
        App.launch(); //ovako pokrecem kod

    }

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(SV.ICON);
        stage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(App.class.getResource(SV.NOTEPADFXML)); //ucitava fxml file i stvara mu sve objekte u memoriju
        AnchorPane anchor = loader.load(); //loader.load() vraca top level container 
        Scene scene = new Scene(anchor);
        this.stage = stage;
        this.anchor = anchor;
        this.scene = scene;
        nc = loader.getController();

        String css = App.class.getResource(SV.CSS).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setOnCloseRequest(this::windowClosing);
        stage.setTitle(SV.IME);
        stage.setScene(scene);
        stage.show();

        new animatefx.animation.LightSpeedIn(anchor).play();
    }

    @Override
    public void stop() {

    }

    public void windowClosing(WindowEvent t) {

        //t.consume(); ako hoces sprijeciti zatvaranje prozora
        nc.menuExitClicked(null);
    }

}
