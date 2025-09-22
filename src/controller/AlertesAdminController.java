package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the administrator alerts view.
 * Manages navigation from the alerts screen to other parts of the admin interface.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 * @version 1.0
 */
public class AlertesAdminController implements Initializable {

    // Label utilisé pour afficher l'heure courante
    @FXML private Label timeLabel;

    /**
     * Initializes the controller after the root element has been processed.
     * Displays the current time on the UI.
     *
     * @param location  URL used to resolve relative paths
     * @param resources ResourceBundle for localized objects
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Affiche l'heure actuelle dans le label prévu
        HeureController.afficherHeure(timeLabel);
    }

    /**
     * Handles a click on the profile icon.
     * Loads the administrator profile view.
     *
     * @param event The MouseEvent triggered by clicking on the profile icon
     */
    @FXML
    private void handleProfilClick(MouseEvent event) {
        // Redirection vers la page ProfilAdmin.fxml
        try {
            GlobalController.switchView("../ressources/fxml/ProfilAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilAdmin : " + e.getMessage());
        }
    }

    /**
     * Handles a click on the dashboard button.
     * Loads the main admin dashboard view.
     *
     * @param event The MouseEvent triggered by clicking the dashboard button
     */
    @FXML
    private void handleAccueil(MouseEvent event) {
        // Redirection vers la page TableauDeBordAdmin.fxml
        try {
            GlobalController.switchView("../ressources/fxml/TableauDeBordAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue TableauDeBordAdmin : " + e.getMessage());
        }
    }

    /**
     * Handles a click on the calendar button.
     * Loads the weekly calendar view for administrators.
     *
     * @param event The MouseEvent triggered by clicking the calendar button
     */
    @FXML
    private void handleCalendrierAdminSemaine(MouseEvent event) {
        // Redirection vers la page CalendrierAdminSemaine.fxml
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierAdminSemaine.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue CalendrierAdminSemaine : " + e.getMessage());
        }
    }

    /**
     * Handles a click on the "Effectif" (staff list) button.
     * Loads the view displaying the list of first-aid workers.
     *
     * @param mouseEvent The MouseEvent triggered by clicking the button
     */
    @FXML
    private void handleEffectif(MouseEvent mouseEvent) {
        // Redirection vers la page ListeDesSecouristesAdmin.fxml
        try {
            GlobalController.switchView("../ressources/fxml/ListeDesSecouristesAdmin.fxml", (Node) mouseEvent.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ListeDesSecouristesAdmin : " + e.getMessage());
        }
    }
}
