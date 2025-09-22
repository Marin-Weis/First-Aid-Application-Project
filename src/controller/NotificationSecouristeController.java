package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the notification view accessible by rescuers.
 * Allows access to contact admins, show calendar, and toggle a popup panel.
 * Also manages redirection to other key views.
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
public class NotificationSecouristeController {

    @FXML private Label timeLabel; // Label pour afficher l'heure en haut de la page

    @FXML
    /**
     * VBox contenant le panneau contextuel (popup) affiché sur demande.
     */
    private VBox popupPane;

    @FXML
    /**
     * Rectangle semi-transparent utilisé comme overlay quand le popup est visible.
     */
    private Rectangle overlay;

    /**
     * Initialisation de la vue : affichage de l'heure en continu.
     */
    @FXML
    public void initialize() {
        HeureController.afficherHeure(timeLabel);
    }

    /**
     * Redirige vers la vue ListeDesAdmin.fxml lorsque l'utilisateur souhaite contacter un administrateur.
     *
     * @param event L'événement déclenché par le clic sur le bouton.
     */
    @FXML
    private void handleContacterAdmin(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ListeDesAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ListeDesAdmin : " + e.getMessage());
        }
    }

    /**
     * Redirige vers la vue TableauDeBord.fxml (page d'accueil du secouriste).
     *
     * @param event L'événement déclenché par le clic.
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
     * Redirige vers la vue CalendrierSecouristeSemaine.fxml.
     *
     * @param mouseEvent L'événement déclenché par le clic.
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
     * Affiche le panneau contextuel (popup) avec un overlay sombre.
     */
    @FXML
    private void showPopup() {
        popupPane.setVisible(true);
        overlay.setVisible(true);
    }

    /**
     * Masque le panneau contextuel (popup) et l’overlay.
     */
    @FXML
    private void hidePopup() {
        popupPane.setVisible(false);
        overlay.setVisible(false);
    }

    /**
     * Redirige vers la vue ProfilSecouriste.fxml (profil du secouriste).
     *
     * @param event L'événement déclenché par le clic.
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
