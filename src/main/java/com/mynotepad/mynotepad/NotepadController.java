package com.mynotepad.mynotepad;

import animatefx.animation.AnimationFX;
import com.mynotepad.mynotepad.helper.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NotepadController implements Initializable {

    @FXML
    private Label lblPokazivac;
    @FXML
    private Label lblZoom;
    @FXML
    private Label lblSustav;
    @FXML
    private Label lblFormat;
    @FXML
    private Label lblFileName;
    @FXML
    private TextArea text;

    @FXML
    private GridPane grid;
    @FXML
    private CheckMenuItem menuStatusBar;
    @FXML
    private VBox vbox;
    @FXML
    private CheckMenuItem menuWordWrap;

    int zoom = 100;
    double fontSize = 20;
    Font font = new Font("Consolas", fontSize);
    File file;
    boolean changed = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        text.setFont(font);
        text.setOnKeyPressed(e -> printCaretPosition());
        text.setOnMouseClicked(e -> printCaretPosition());
        lblZoom.setText(zoom + SV.POSTO);
        lblFormat.setText(SV.UTF8); //TODO check how to detect encoding type
        lblSustav.setText(SV.WCRLF);
        lblPokazivac.setText(SV.POKAZIVAC);
        lblFileName.setText(SV.FILENAME);
        menuStatusBar.setSelected(true);
        text.textProperty().addListener((ov, t, t1) -> { //ov promatrani objekt (tekst), t stari tekst, t1 promjenjeni tekst (oboje kao stringovi) 
            changed = true; //ako je changed onda pitamo da sejva pri zatvaranju 
        });

        text.setOnDragOver((event) -> {

            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        });
        text.setOnDragDropped((event) -> {
            //moramo definirati dragboard
            Dragboard db = event.getDragboard();
            boolean sucess = false;
            if (db.hasFiles()) {
                sucess = true;
                System.out.println(db.getFiles().toString());
            }
            event.setDropCompleted(sucess);
            if (sucess) {
                text.setText("");

                try {
                    file = db.getFiles().get(0);
                    openFile(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    @FXML
    private void menuNewClicked(ActionEvent event) {
        if (changed) {
            if (file == null) {
                askToSave();
            } else {
                try {
                    saveToFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        text.setText("");
    }

    @FXML
    private void menuNewWindowClicked(ActionEvent event) {

        try {
            //        Platform.runLater(() -> {
//
//            try {
//                App app = new App();
//                app.start(new Stage());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });

            new ProcessBuilder("NotepaD.exe").start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void menuOpenClicked(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(SV.INITIALDIR));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("HTML files", "*.html"),
                new FileChooser.ExtensionFilter("Java files", "*.java"),
                new FileChooser.ExtensionFilter("XML files", "*.xml")
        );
        file = fc.showOpenDialog(App.stage);
        if (file != null) {
            try {
                openFile(file);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @FXML
    private void menuSaveClicked(ActionEvent event) {

        if (!changed) {
            return;
        }
        if (file == null) {
            saveAs();
        } else {
            try {
                saveToFile();
            } catch (IOException ex) {
                ex.printStackTrace(); //TODO handle better if file cannot be written
            }
        }
        changed = false;
    }

    @FXML
    private void menuSaveAsClicked(ActionEvent event) {
        saveAs();
        changed = false;

    }

    @FXML
    private void menuPageSetupClicked(ActionEvent event) {
    }

    @FXML
    private void menuPrintClicked(ActionEvent event) {
    }

    @FXML
    public void menuExitClicked(ActionEvent event) {
        if (changed) {
            askToSave();
        }
        Platform.exit();
    }

    @FXML
    private void menuUndoClicked(ActionEvent event) {
        text.undo();
    }

    @FXML
    private void menuCutClicked(ActionEvent event) {
        text.cut();

    }

    @FXML
    private void menuCopyClicked(ActionEvent event) {
        text.copy();
    }

    @FXML
    private void menuPasteClicked(ActionEvent event) {
        text.paste();
    }

    @FXML
    private void menuDeleteClicked(ActionEvent event) {
        text.deleteText(text.getSelection());
    }

    @FXML
    private void menuSwGClicked(ActionEvent event) {
        String temp = text.getSelectedText();
        try {
            new ProcessBuilder("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe", "www.google.com/search?q=" + temp).start();
//            new ProcessBuilder("notepad.exe").start();
//            try {
//                Thread.sleep(1000);
//                Robot robot = new Robot();
////                robot.mouseMove(300, 400);
////                robot.mousePress(MouseButton.PRIMARY);
////                robot.mouseRelease(MouseButton.PRIMARY);
////                robot.mouseMove(100, 100);
////                robot.mousePress(MouseButton.PRIMARY);
////                robot.mouseRelease(MouseButton.PRIMARY);
//                robot.keyType(KeyCode.S);
//                Thread.sleep(500);
//                robot.keyType(KeyCode.A);
//                Thread.sleep(500);
//                robot.keyType(KeyCode.D);
//                Thread.sleep(500);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuFindClicked(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("find.fxml"));
            DialogPane dp = loader.load();
            FindController fc = loader.getController();
            fc.setText(text);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dp);
            dialog.setTitle("Find");
            dialog.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void menuFindNextClicked(ActionEvent event) {
    }

    @FXML
    private void menuFindPreviousClicked(ActionEvent event) {
    }

    @FXML
    private void menuReplaceClicked(ActionEvent event) {
    }

    @FXML
    private void menuGoToClicked(ActionEvent event) {
    }

    @FXML
    private void menuSelectAllClicked(ActionEvent event) {
        text.selectAll();
    }

    @FXML
    private void menuTimeDateClicked(ActionEvent event) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();

        text.insertText(text.getCaretPosition(), dtf.format(ldt));
    }

    @FXML
    private void menuWordWrapClicked(ActionEvent event) {

        text.setWrapText(menuWordWrap.isSelected());
    }

    @FXML
    private void menuFontClicked(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("font.fxml")); //FXMLLoad loada nas font.fxml
            DialogPane dialogPane = loader.load(); //loader.load() vraca top level container (dialogpane), nije prikazan do dialog.showandwait()
            String css = App.class.getResource(SV.CSS).toExternalForm();
            dialogPane.getStylesheets().add(css);
            FontController fc = loader.getController(); //dohvacamo controller ovako i koristimo ga
            fc.setFont(text.getFont());
            new animatefx.animation.RotateIn(dialogPane).play();

            Dialog<ButtonType> dialog = new Dialog<>(); //top level container kao scene za dialogpane
            dialog.setDialogPane(dialogPane); //setiramo dialogpane u dialog
            dialog.setTitle("Set font...");
            Optional<ButtonType> buttonClicked = dialog.showAndWait(); //program ne ide dalje dok ne zatvorimo dijalog na ovaj ili onaj nacin i vraca dugme koje smo kliklnuli
            if (buttonClicked.get() == ButtonType.OK) {
                text.setFont(fc.getFont());
                System.out.println("Pritisnio si OK dugme");
            } else if (buttonClicked.get() == ButtonType.CANCEL) {
                System.out.println("Pritisnio si Cancel");
            } else {
                System.out.println("Nisi pritisnio nista");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void menuZoomInClicked(ActionEvent event) {

        font = new Font(font.getName(), font.getSize() + 1);
        text.setFont(font);
        zoom += 10;
        lblZoom.setText(zoom + SV.POSTO);
    }

    @FXML
    private void menuZoomOutClicked(ActionEvent event) {
        font = new Font(font.getName(), font.getSize() - 1);
        text.setFont(font);
        zoom -= 10;
        lblZoom.setText(zoom + SV.POSTO);

    }

    @FXML
    private void menuRestoreDefaultZoomClicked(ActionEvent event) {
        font = new Font(font.getName(), fontSize);
        text.setFont(font);
        zoom = 100;
        lblZoom.setText(zoom + SV.POSTO);
    }

    @FXML
    private void menuStatusBarClicked(ActionEvent event) {

        if (menuStatusBar.isSelected()) {
//            grid.setVisible(true);
            vbox.getChildren().add(1, grid);
        } else {
//            grid.setVisible(false);
            vbox.getChildren().remove(grid);
        }
    }

    @FXML
    private void menuViewHelpClicked(ActionEvent event) {
    }

    @FXML
    private void menuSendFeedbackClicked(ActionEvent event) {
    }

    @FXML
    private void menuAboutNotepadClicked(ActionEvent event) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About MyNotepad");
        alert.setHeaderText("Created by");
        alert.setContentText("Tri mlada poluprogramera");
        alert.setGraphic(new ImageView(new Image("icon.png")));
        String css = App.class.getResource(SV.CSS).toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);
        alert.showAndWait();
    }

    private void openFile(File file) throws IOException {
        String temp = "";
        String line = "";

        try ( FileReader fr = new FileReader(file);  BufferedReader br = new BufferedReader(fr)) {
            while ((line = br.readLine()) != null) {

                temp = temp + line + "\n"; //temp+= line;
            }
        }
        if (temp.contains("\n")) {
            lblSustav.setText(SV.WCRLF);
        } else {
            lblSustav.setText(SV.ULF);
        }
        text.setText(temp);
        Platform.runLater(() -> lblFileName.setText("File: " + file.getAbsolutePath()));
    }

    private void printCaretPosition() {

        CaretPosition cp = new CaretPosition(text);
        lblPokazivac.setText("Ln " + cp.getLine() + ", Col " + cp.getColumn() + ", Index " + cp.getIndex());

    }

    private void saveAs() {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(SV.INITIALDIR));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt"),
                new FileChooser.ExtensionFilter("HTML files", "*.html"),
                new FileChooser.ExtensionFilter("Java files", "*.java"),
                new FileChooser.ExtensionFilter("XML files", "*.xml")
        );
        file = fc.showSaveDialog(App.stage);
        if (file != null) {
            try {
                saveToFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void saveToFile() throws IOException {

        if (file == null) {
            return;
        }

        try ( FileWriter fw = new FileWriter(file);  BufferedWriter bw = new BufferedWriter(fw)) {

            fw.write(text.getText());
            bw.flush();
        }

    }

    public void askToSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Current file modified");
        alert.setContentText("Do you want to save your file?");
        String css = App.class.getResource(SV.CSS).toExternalForm();
        alert.getDialogPane().getStylesheets().add(css);

        ButtonType yes = new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("NO", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yes, no, cancel);
        alert.showAndWait().ifPresent((t) -> {
            if (t.getText().equals("YES")) {
                if (file == null) {
                    saveAs();
                } else {
                    try {
                        saveToFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (t.getText().equals("NO")) {

            } else {

            }
        });
    }

    public TextArea getText() {
        return text;
    }

}
