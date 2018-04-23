package tictactoe;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tictactoe.gui.MainController;
import tictactoe.gui.PlateauController;
import tictactoe.gui.PointageController;
import tictactoe.gui.TextStatusController;

import java.io.IOException;

public class Main extends Application {
    private static final String TITRE = "Jeu de Tic-Tac-Toe";

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle(TITRE);

        JeuHumainContreHumain jeu = new JeuHumainContreHumain(); //Créer le jeu

        MainController controller = new MainController(jeu); //Créer le controller de l'interface

        //Créer l'interface
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        fxmlLoader.setControllerFactory(param -> {
            if (param == MainController.class) return new MainController(jeu);
            if (param == PointageController.class) return new PointageController(jeu);
            if (param == TextStatusController.class) return new TextStatusController(jeu);
            if (param == PlateauController.class) return new PlateauController(jeu);

            else {
                try {
                    return param.newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc);
                }
            }
        });

        //Montrer l'interface
        primaryStage.setScene(new Scene(fxmlLoader.load()));
//        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
