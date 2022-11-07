package com.mynotepad.mynotepad.helper;

import javafx.scene.control.TextArea;

public class CaretPosition {

    private int line = 1;
    private int column = 1;
    private int index = 0;

    public CaretPosition(TextArea text) {

        index = text.getCaretPosition();
        for (int i = 0; i < text.getCaretPosition(); i++) {

            column++;
            if (text.getText().charAt(i) == '\n') {
                line++;
                column = 1;
            }
        }
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getIndex() {
        return index;
    }
}
