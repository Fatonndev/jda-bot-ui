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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import ru.kevitv.obvilionNetwork.utils.Config;
import ru.kevitv.obvilionNetwork.bot.Bot;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    // Left side
    @FXML public VBox pnItems = null;
    @FXML public Button btnStatistics;
    @FXML public Button btnSystemUsage;
    @FXML public Button btnCommands;
    @FXML public Button btnConsole;
    @FXML public Button btnPackages;
    @FXML public Button btnSettings;
    @FXML public Button btnExit;
    @FXML public Pane pnlCommands;
    @FXML public Pane pnlSystemUsage;
    @FXML public Pane pnlStatistics;
    @FXML public Pane pnlConsole;

    // Up side
    @FXML public Label totalServers;
    @FXML public Label totalPlayers;
    @FXML public Label premiumServers;
    @FXML public Label timeOnline;

    // Other
    @FXML public Label output;
    @FXML public Label ping;

    // System usage
    @FXML public Arc arc1;
    @FXML public Arc arc2;
    @FXML public Arc arc3;
    @FXML public Arc arc4;

    @FXML public Label percentage1;
    @FXML public Label percentage2;
    @FXML public Label percentage3;
    @FXML public Label percentage4;

    @FXML public LineChart cpuChart;
    @FXML public LineChart ramChart;

    @FXML public Label cpuName;
    @FXML public Label ramName;

    public List<Node> nodes = new ArrayList<>();
    public List<FXMLLoader> loaders = new ArrayList<>();
    public String line = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    public void update() {

        // Очистка от хлама
        pnItems.getChildren().clear();
        loaders.clear();
        nodes.clear();

        // Добавляем элементы на экран
        for (int i = 0; i < Bot.bot.getGuilds().size(); i++) {
            try {

                if(i >= Config.maxNodes) return;

                loaders.add(new FXMLLoader(getClass().getResource("Item.fxml")));
                nodes.add(loaders.get(i).load());

                // Добавляем эффект при наведении мышкой
                int j = i;
                nodes.get(i).setOnMouseEntered(event -> {
                    nodes.get(j).setStyle("-fx-background-color : #0A0E3F");
                });
                nodes.get(i).setOnMouseExited(event -> {
                    nodes.get(j).setStyle("-fx-background-color : #02030A");
                });
                pnItems.getChildren().add(nodes.get(i));

            } catch (IOException e) {
                if (Config.nogui) {
                    ErrorWindow.create("Item.fxml not found");
                }
                e.printStackTrace();
            }
        }

        totalPlayers.setText(String.valueOf(Bot.bot.getUsers().size()));
        totalServers.setText(String.valueOf(Bot.bot.getGuilds().size()));
        premiumServers.setText("0");

        long time = (System.currentTimeMillis() - Config.startTime)/1000/60;
        if(time > 60) {
            if (time > 60 * 24) {
                timeOnline.setText(time / 60 / 24 + "D");
            } else {
                timeOnline.setText(time / 60 + "H");
            }
        } else {
            timeOnline.setText(time + "M");
        }

    }

    public void updConsole() throws FileNotFoundException {
        File text = new File("LOG.log");
        Scanner scnr = new Scanner(text);

        line = "";

        while(scnr.hasNextLine()){
            if (!line.isEmpty())
                line = line +"\n"+ scnr.nextLine();
            else line = line + scnr.nextLine();
        }

        output.setText(line);
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnCommands) {
            pnlCommands.setStyle("-fx-background-color : #1620A1");
            pnlCommands.toFront();
        }

        if (actionEvent.getSource() == btnConsole) {
            pnlConsole.toFront();
        }

        if (actionEvent.getSource() == btnStatistics) {
            pnlStatistics.setStyle("-fx-background-color : #02030A");
            pnlStatistics.toFront();
        }

        if (actionEvent.getSource()== btnSystemUsage) {
            pnlSystemUsage.toFront();
        }

        if (actionEvent.getSource()==btnExit) {
            System.exit(0);
        }
    }
}
