package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import metier.graphe.model.dao.SecouristeDAO;
import metier.persistence.Secouriste;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the admin view listing all rescuers.
 * Responsible for loading and displaying rescuer cards,
 * and handling navigation to different parts of the admin interface.
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
public class ListeDesSecouristesAdminController {

    @FXML private Label timeLabel; // Label affichant l’heure en haut de la vue

    @FXML private VBox eventList;  // Conteneur principal pour afficher les cartes des secouristes

    /**
     * Initializes the view by displaying the current time and loading rescuers.
     */
    public void initialize() {
        // Affiche l'heure actuelle en haut de la vue
        HeureController.afficherHeure(timeLabel);

        // Charge tous les secouristes depuis la base et crée les cartes
        loadSecouristes();
    }

    /**
     * Loads and displays a card for each rescuer in the database.
     */
    private void loadSecouristes() {
        SecouristeDAO secouristeDAO = new SecouristeDAO();
        List<Secouriste> secouristes = secouristeDAO.findAll();

        for (Secouriste s : secouristes) {
            eventList.getChildren().add(createSecouristeCard(s));
        }
    }

    /**
     * Creates a visual card for a rescuer with name, avatar, and role.
     *
     * @param s The rescuer to display
     * @return A JavaFX Node representing the card
     */
    private Node createSecouristeCard(Secouriste s) {
        HBox card = new HBox(15); // Carte alignée horizontalement avec un espacement
        card.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 25;");
        card.setPadding(new Insets(10, 20, 10, 10));
        card.setPrefWidth(360);
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Avatar du secouriste
        ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/ressources/img/avatar.png")));
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setClip(new javafx.scene.shape.Circle(30, 30, 30)); // Rend l’image circulaire

        // Texte : prénom + nom
        Label nomPrenom = new Label(s.getPrenom() + "  " + s.getNom());
        nomPrenom.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: black;");

        // Texte : rôle
        Label role = new Label("Secouriste professionnel");
        role.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        VBox infoBox = new VBox(5, nomPrenom, role); // Conteneur vertical pour le texte

        // Bouton image (flèche)
        ImageView arrow = new ImageView(new Image(getClass().getResourceAsStream("/ressources/img/bouton_liste_admin.png")));
        arrow.setFitWidth(28);
        arrow.setFitHeight(28);

        // Lors du clic sur la flèche, on redirige vers le profil du secouriste
        arrow.setOnMouseClicked(event -> {
            try {
                GlobalController.setSelectedSecouriste(s);
                GlobalController.switchView("../ressources/fxml/ProfilSecouristeAdmin.fxml", (Node) event.getSource());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Region spacer = new Region(); // Permet de pousser la flèche à droite
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        card.getChildren().addAll(avatar, infoBox, spacer, arrow);
        return card;
    }

    // ==== MÉTHODES DE NAVIGATION ADMIN ====

    /**
     * Navigates to the admin dashboard view.
     * @param event MouseEvent triggered by the user.
     */
    @FXML
    private void handleAccueil(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/TableauDeBordAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue TableauDeBordAdmin : " + e.getMessage());
        }
    }

    /**
     * Navigates to the admin alerts view.
     * @param event MouseEvent triggered by the user.
     */
    @FXML
    private void handleAlertesAdmin(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/AlertesAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue AlertesAdmin : " + e.getMessage());
        }
    }

    /**
     * Navigates to the weekly calendar view for admins.
     * @param event MouseEvent triggered by the user.
     */
    @FXML
    private void handleCalendrierAdminSemaine(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierAdminSemaine.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue CalendrierAdminSemaine : " + e.getMessage());
        }
    }


    /**
     * Navigates to the profile view of the current admin.
     * @param event MouseEvent triggered by the user.
     */
    @FXML
    private void handleProfilClick(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilAdmin : " + e.getMessage());
        }
    }

    /**
     * Navigates to the profile view of the selected rescuer.
     * @param event MouseEvent triggered by the user.
     */
    @FXML
    private void showProfil(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilSecouristeAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilSecouristeAdmin : " + e.getMessage());
        }
    }
}
