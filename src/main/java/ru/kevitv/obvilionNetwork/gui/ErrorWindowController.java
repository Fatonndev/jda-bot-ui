/*
 * Copyright 2020 KeviTV.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.kevitv.obvilionNetwork.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ErrorWindowController implements Initializable  {

    @FXML public Label consoleText;
    @FXML public Label errorInfo;
    @FXML public Button okButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consoleText.setText(loadLog());

        okButton.setOnAction(event -> {
            System.exit(0);
        });
    }

    public String loadLog() {
        try {
            File text = new File("LOG.log");
            Scanner scnr = new Scanner(text);

            String line = "";
            while(scnr.hasNextLine()){
                if (!line.isEmpty())
                    line = line +"\n"+ scnr.nextLine();
                else line = line + scnr.nextLine();
            }

            return line;
        } catch (FileNotFoundException e) {

        }

        return "File Not Found";
    }
}
