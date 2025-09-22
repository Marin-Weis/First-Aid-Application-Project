package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import metier.graphe.model.dao.SecouristeDAO;
import metier.persistence.Secouriste;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the rescuer's personal information editing view.
 * Allows a rescuer to view and update their profile information: name, phone number, email.
 * Includes validation for names and phone format, and handles view navigation.
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
public class InfosSecouristeController implements Initializable {

    @FXML private Label timeLabel;               // Label de l'heure en haut de l'écran
    @FXML private ScrollPane scrollPane;         // Scroll pour la fiche secouriste
    @FXML private Button backButton;             // Bouton retour
    @FXML private TextField nomField;            // Champ de saisie du nom
    @FXML private TextField prenomField;         // Champ de saisie du prénom
    @FXML private TextField adresseField;        // Champ de saisie de l'adresse email
    @FXML private TextField telephoneField;      // Champ de saisie du téléphone

    private Secouriste utilisateur;              // Utilisateur connecté
    private Secouriste utilisateurOriginal;      // (Optionnel) pour gérer l’annulation, non utilisé ici

    /**
     * Initializes the controller and fills the form with the current user's info.
     * Also binds scrolling behavior for smooth scroll experience.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Affichage de l'heure en haut
        HeureController.afficherHeure(timeLabel);

        // Récupère l'utilisateur connecté
        utilisateur = GlobalController.getCurrentUser();

        // Pré-remplit les champs avec les infos de l'utilisateur
        if (utilisateur != null) {
            nomField.setText(utilisateur.getNom());
            prenomField.setText(utilisateur.getPrenom());
            adresseField.setText(utilisateur.getEmail());
            telephoneField.setText(utilisateur.getTelephone() != null ? utilisateur.getTelephone() : "");
        }

        // Amélioration du scroll avec la souris
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });
    }

    /**
     * Returns to the previous profile view when the back button is clicked.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilSecouriste : " + e.getMessage());
        }
    }

    /**
     * Validates and saves the rescuer’s modified profile data to the database.
     */
    @FXML
    private void handleSaveProfil(ActionEvent event) {
        String nouveauNom = nomField.getText().trim();
        String nouveauPrenom = prenomField.getText().trim();
        String nouveauTelephone = telephoneField.getText().trim();

        // Validation du nom et prénom (lettres, tirets, espaces)
        if (!estNomValide(nouveauNom) || !estNomValide(nouveauPrenom)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Le nom et le prénom ne doivent contenir que des lettres, des tirets ou des espaces.");
            alert.showAndWait();
            return;
        }

        // Validation du numéro de téléphone (XX XX XX XX XX)
        if (!nouveauTelephone.isEmpty() && !estTelephoneValide(nouveauTelephone)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Format incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Le numéro de téléphone doit être au format : XX XX XX XX XX.");
            alert.showAndWait();
            return;
        }

        // Mise à jour des données de l'utilisateur
        utilisateur.setNom(nouveauNom);
        utilisateur.setPrenom(nouveauPrenom);
        utilisateur.setEmail(adresseField.getText());
        utilisateur.setTelephone(nouveauTelephone);

        // Enregistre les modifications dans la base
        SecouristeDAO dao = new SecouristeDAO();
        dao.update(utilisateur);

        System.out.println("Coordonnées mises à jour.");

        // Message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Les coordonnées ont été mises à jour avec succès.");
        alert.showAndWait();

        // Retour à la vue précédente
        try {
            GlobalController.switchView("../ressources/fxml/ProfilSecouriste.fxml", (Node) event.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilSecouriste : " + e.getMessage());
        }
    }

    /**
     * Checks if a name is valid (letters, dashes, spaces allowed).
     *
     * @param nom the name to validate
     * @return true if valid
     */
    private boolean estNomValide(String nom) {
        return nom.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$");
    }

    /**
     * Checks if the phone number format is valid (XX XX XX XX XX).
     *
     * @param numero the phone number
     * @return true if format is valid
     */
    private boolean estTelephoneValide(String numero) {
        return numero.matches("^\\d{2}( \\d{2}){4}$");
    }
}
