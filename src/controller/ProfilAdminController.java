package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;
import metier.persistence.Admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de la vue de profil pour un administrateur.
 * Affiche les informations personnelles de l'administrateur connecté.
 * Gère la déconnexion et le retour à la vue précédente via une animation.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 * @version 1.0
 */
public class ProfilAdminController implements Initializable {

    @FXML private Label timeLabel;

    @FXML
    /** Composant permettant le défilement de l'écran */
    private ScrollPane scrollPane;

    @FXML
    /** Bouton de retour à la vue précédente */
    public Button backButton;

    // Champs pour afficher les données personnelles de l'admin
    @FXML private Label nomField;
    @FXML private Label prenomField;
    @FXML private Label adresseField;
    @FXML private Label telephoneField;

    /**
     * Méthode appelée à l'initialisation de la vue.
     * Elle affiche l'heure actuelle et les informations de l'admin.
     * Elle personnalise aussi la vitesse de défilement du ScrollPane.
     *
     * @param location L'emplacement utilisé pour résoudre les chemins relatifs (non utilisé ici).
     * @param resources Les ressources utilisées pour l'internationalisation (non utilisé ici).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Affiche l'heure en continu
        HeureController.afficherHeure(timeLabel);

        // Accélère la vitesse de défilement vertical
        this.scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        // Récupère l'utilisateur admin actuellement connecté
        Admin admin = GlobalController.getCurrentAdmin();
        if (admin != null) {
            nomField.setText(admin.getNom());
            prenomField.setText(admin.getPrenom());
            adresseField.setText(admin.getEmail());
            telephoneField.setText("Non disponible"); // Le téléphone n'est pas stocké pour l'admin
        }
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Retour".
     * Remplace la scène courante par la vue précédente enregistrée.
     *
     * @param event L'événement de clic déclenché par le bouton.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        if (!GlobalController.getViewHistory().isEmpty()) {
            // On récupère la vue précédente
            Parent previousView = GlobalController.getViewHistory().pop();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(previousView);

            // Animation de transition depuis la gauche
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), previousView);
            transition.setFromX(-scene.getWidth());
            transition.setToX(0);
            transition.play();
        }
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Déconnexion".
     * Redirige vers l'écran d'accueil de l'application.
     *
     * @param event L'événement déclenché par le clic.
     */
    @FXML
    private void handleDeconnexion(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/Accueil.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue Accueil : " + e.getMessage());
        }
    }
}
