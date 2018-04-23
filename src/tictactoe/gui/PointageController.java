package tictactoe.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import tictactoe.Jeu;

/**
 * Controlle la zone qui montre les points
 */
public class PointageController {
    private static final String UNITE_POINT = "pt";

    @FXML
    private Text textScoreCercle;
    @FXML
    private Text textScoreCroix;

    private final SimpleIntegerProperty scoreCercle = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty scoreCroix = new SimpleIntegerProperty(0);

    public PointageController(Jeu jeu) {
        //Si la propriété de status de jeu change...
        jeu.jeuStatusProperty().addListener(
                (observable, oldValue, newValue) -> {
                    switch (newValue) {
                        //Si les X gagne, +1 pour les X
                        case X_GAGNE:
                            scoreCroix.set(scoreCroix.getValue() + 1);
                            break;
                        //Si les O gagne +1 pour les O
                        case O_GAGNE:
                            scoreCercle.set(scoreCercle.getValue() + 1);
                            break;
                        case EGALITE:
                            scoreCroix.set(scoreCroix.getValue() + 1);
                            scoreCercle.set(scoreCercle.getValue() + 1);
                            break;
                    }
                }
        );
    }

    @FXML
    public void initialize() {
        //Attacher la boite de texte avec le score propriété de score
        //Bindings.concat() pour mettre tout ajouter les unités
        textScoreCercle.textProperty().bind(Bindings.concat(scoreCercle, " ", UNITE_POINT));
        textScoreCroix.textProperty().bind(Bindings.concat(scoreCroix, " " , UNITE_POINT));
    }
}
