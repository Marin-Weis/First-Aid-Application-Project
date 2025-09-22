package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the home view of the application.
 * It handles navigation to the login and registration interfaces.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 * @version 1.0
 */
public class AccueilController implements Initializable {

    // Label pour afficher l'heure actuelle
    @FXML private Label timeLabel;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Displays the current time in the timeLabel.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Affiche l'heure courante dans le label prévu à cet effet
        HeureController.afficherHeure(timeLabel);
    }

    /**
     * Handles the action of clicking the "Connexion" button.
     * Switches the current view to the login screen.
     *
     * @param event the event triggered by clicking the button
     */
    @FXML
    private void handleConnexion(ActionEvent event) {
        // On récupère la scène actuelle à partir de l'élément source de l'événement
        // event.getSource() est le bouton qui a été cliqué
        try {
            GlobalController.switchView("../ressources/fxml/Connexion.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace(); // Affiche la trace complète de l'erreur
            System.out.println("Erreur lors du chargement de la vue Connexion : " + e.getMessage());
        }
    }

    /**
     * Handles the action of clicking the "Inscription" button.
     * Switches the current view to the registration screen.
     *
     * @param event the event triggered by clicking the button
     */
    @FXML
    private void handleInscription(ActionEvent event) {
        // Même principe que pour la connexion, mais redirige vers la création de compte
        try {
            GlobalController.switchView("../ressources/fxml/Creation.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace(); // Affiche l'erreur complète dans la console
            System.out.println("Erreur lors du chargement de la vue Inscription : " + e.getMessage());
        }
    }

}
