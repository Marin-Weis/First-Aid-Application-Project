package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the view listing all admins.
 * Displays the current time, manages popup behavior, and handles navigation between views.
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
public class ListeDesAdminController implements Initializable {

    @FXML private Label timeLabel; // Affiche l'heure en haut de l'écran

    @FXML
    private VBox popupPane; // Panneau de la popup (masqué par défaut)

    @FXML
    private Rectangle overlay; // Fond sombre derrière la popup

    /**
     * Initializes the view by displaying the current time.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Affiche l'heure dans la barre supérieure
        HeureController.afficherHeure(timeLabel);
    }

    /**
     * Shows the popup and overlay when a button is clicked.
     */
    @FXML
    private void showPopup() {
        popupPane.setVisible(true);   // Affiche le contenu de la popup
        overlay.setVisible(true);     // Affiche le fond sombre derrière
    }

    /**
     * Hides the popup and overlay when closing.
     */
    @FXML
    private void hidePopup() {
        popupPane.setVisible(false);  // Cache le contenu
        overlay.setVisible(false);    // Cache le fond sombre
    }

    /**
     * Navigates back to the main dashboard view for the rescuer.
     */
    @FXML
    private void handleTableauDeBord(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/TableauDeBord.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue TableauDeBord : " + e.getMessage());
        }
    }

    /**
     * Navigates to the weekly calendar view for the rescuer.
     */
    @FXML
    private void handleCalendrierSecouriste(MouseEvent mouseEvent) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierSecouristeSemaine.fxml", (Node) mouseEvent.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue CalendrierSecouristeSemaine : " + e.getMessage());
        }
    }

    /**
     * Navigates to the rescuer's profile view.
     */
    @FXML
    private void handleProfilClick(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilSecouriste : " + e.getMessage());
        }
    }
}
