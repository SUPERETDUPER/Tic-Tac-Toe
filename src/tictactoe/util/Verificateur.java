/*
 * MIT License
 *
 * Copyright (c) 2018 Martin Staadecker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package tictactoe.util;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;
import tictactoe.Jeu;

import java.util.Iterator;
import java.util.List;

/**
 * Class qui vérifie pour des gagnants ou en status d'égalité puis si c'est le cas notifie le listener (dans notre cas notifie le jeu)
 * <p>
 * Au lieu de revérifier l'état du plateau après chaque tour, le listener vérifie que la rangée, colonne au diagonale qui a été modifié
 * Pour détecter des égalité, le verificateur compte le nombre de boites remplies (non-vide).
 */
public class Verificateur implements ChangeListener<Jeu.JeuStatus> {

    private final ReadOnlyObjectWrapper<Jeu.JeuStatus> status = new ReadOnlyObjectWrapper<>(Jeu.JeuStatus.INCOMPLET);

    private final int nombreDeLignes;
    private int nombreDeEgalite = 0;

    @SuppressWarnings("ConstantConditions")
    public Verificateur(@NotNull StructurePlateau<ReadOnlyObjectProperty<Jeu.BoiteStatus>> statusBoite) {
        int nombreDeLignes = 0;

        //Pour chaque rangée créé un vérificateur de ligne
        Iterator<List<ReadOnlyObjectProperty<Jeu.BoiteStatus>>> iteratorRangee = statusBoite.iteratorRangee();

        while (iteratorRangee.hasNext()) {
            new VerificateurLigne(iteratorRangee.next()).statusProperty().addListener(this);
            nombreDeLignes++;
        }

        //Créé un vérificateur de ligne pour chaque colonne
        Iterator<List<ReadOnlyObjectProperty<Jeu.BoiteStatus>>> iteratorColonne = statusBoite.iteratorColonne();

        while (iteratorColonne.hasNext()) {
            new VerificateurLigne(iteratorColonne.next()).statusProperty().addListener(this);
            nombreDeLignes++;
        }

        //Crée un vérificateur de ligne pour les deux diagonales
        new VerificateurLigne(statusBoite.getDiagonaleGaucheDroit()).statusProperty().addListener(this);
        new VerificateurLigne(statusBoite.getDiagonaleDroiteGauche()).statusProperty().addListener(this);
        nombreDeLignes += 2;

        this.nombreDeLignes = nombreDeLignes;

//        for (Position position : statusBoite) {
//            //Necessaire pour que le compter boiteVide commence avec le bon chiffre
//            if (statusBoite.get(position).get() != BoiteController.BoiteStatus.VIDE) {
//                boiteVide--;
//            }
//
//            //Ajouter un listener pour être notifié quand les boites changent pour pouvoir détecter les égalités
//            statusBoite.get(position).addListener(this);
//        }
    }

    @Override
    public void changed(ObservableValue<? extends Jeu.JeuStatus> observable, Jeu.JeuStatus oldValue, Jeu.JeuStatus newValue) {
        if (oldValue == Jeu.JeuStatus.EGALITE) {
            nombreDeEgalite--;
        }

        if (newValue == Jeu.JeuStatus.EGALITE) {
            nombreDeEgalite++;
        }

        switch (newValue) {
            case INCOMPLET:
            case CERCLE_GAGNE:
            case CROIX_GAGNE:
                status.set(newValue);
                return;
        }

        if (nombreDeEgalite == nombreDeLignes) {
            status.set(Jeu.JeuStatus.EGALITE);
        }
    }

    public ReadOnlyObjectProperty<Jeu.JeuStatus> statusProperty() {
        return status.getReadOnlyProperty();
    }
}
