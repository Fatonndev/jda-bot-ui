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
