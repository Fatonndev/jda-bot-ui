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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.kevitv.obvilionNetwork.utils.Config;

public class ErrorWindow extends Application {
    public static String err;
    private double x, y;

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Error.fxml"));
            Parent root = loader.load();

            ErrorWindowController controller = loader.getController();
            controller.errorInfo.setText(err);

            primaryStage.setScene(new Scene(root));
            primaryStage.initStyle(StageStyle.UNDECORATED);

            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });
            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - x);
                primaryStage.setY(event.getScreenY() - y);
            });

            primaryStage.show();
        } catch (Exception e) {

        }
    }

    public static void create(String err) {
        ErrorWindow.err = err;
        main(Config.args);
    }

    public static void main (String[] args) {
        Platform.setImplicitExit(false);
        launch(args);
    }
}
