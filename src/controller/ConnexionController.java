/**
 * Controller for the login (connexion) view.
 * Handles password visibility toggle, "remember me" feature, and user authentication
 * (both admin and secouriste).
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 * @version 1.0
 */
package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import metier.graphe.model.dao.AdminDAO;
import metier.persistence.Admin;
import metier.persistence.Secouriste;
import metier.graphe.model.dao.SecouristeDAO;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the login (connexion) view.
 * Handles the user authentication for both Admin and Secouriste roles,
 * password visibility toggling, and the "remember me" feature.
 *
 * This class also supports UI behavior for interactive feedback
 * and preserves user login input locally using a text file.
 *
 * @author M. Weis
 * @author N. Lescop
 * @author M. Gouelo
 * @author A. Jan
 * @version 1.0
 */
public class ConnexionController {

    // Label pour afficher l'heure courante
    @FXML private Label timeLabel;

    // Champ masqué pour le mot de passe (par défaut)
    @FXML private PasswordField passwordField;

    // Champ visible pour afficher le mot de passe en clair
    @FXML private TextField visiblePasswordField;

    // Icône œil pour basculer visibilité du mot de passe
    @FXML private ImageView toggleEye;

    // État de visibilité du mot de passe
    private boolean passwordVisible = false;

    @FXML public Button backButton; // Bouton retour vers Accueil

    @FXML private CheckBox customCheckbox; // Checkbox "se souvenir de moi"
    @FXML private ImageView checkboxImage; // Image associée à la checkbox

    private Image checkedImage;   // Image case cochée
    private Image uncheckedImage; // Image case non cochée

    @FXML private TextField emailField;

    // Chemin du fichier local pour sauvegarder l'email
    private final String FILE_PATH = "remember_me.txt";

    /**
     * Initializes the controller: shows the current time, loads the saved email
     * if the "remember me" box was previously checked, and configures checkbox visuals.
     */
    @FXML
    public void initialize() {
        HeureController.afficherHeure(timeLabel); // Affichage heure dynamique
        loadRememberedEmail(); // Préremplit le champ email si enregistré

        // Chargement des icônes
        checkedImage = new Image(getClass().getResource("/ressources/img/case_coche.png").toExternalForm());
        uncheckedImage = new Image(getClass().getResource("/ressources/img/case_non_coche.png").toExternalForm());

        checkboxImage.setImage(uncheckedImage); // Par défaut : décoché

        // Lien dynamique entre checkbox et son image
        customCheckbox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            checkboxImage.setImage(isNowSelected ? checkedImage : uncheckedImage);
        });
    }

    /**
     * Handles the back button click and navigates the user to the welcome (Accueil) view.
     *
     * @param event the action event triggered by the back button
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/Accueil.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de retour à Accueil : " + e.getMessage());
        }
    }

    // ==== Gestion des effets visuels du bouton retour ====

    /**
     * Handles mouse hover effect on the back button (opacity change).
     */
    @FXML private void onBackHover()    { backButton.setOpacity(0.7); }

    /**
     * Restores the original appearance of the back button when the mouse exits.
     */
    @FXML private void onBackExit()     { backButton.setOpacity(1.0); }

    /**
     * Applies a visual press effect to the back button (translation and opacity).
     */
    @FXML private void onBackPress()    { backButton.setTranslateY(2); backButton.setOpacity(0.5); }

    /**
     * Removes the press effect when the back button is released.
     */
    @FXML private void onBackRelease()  { backButton.setTranslateY(0); backButton.setOpacity(0.7); }

    /**
     * Toggles the visibility of the password field (masqué <-> visible).
     */
    @FXML
    private void togglePasswordVisibility() {
        if (passwordVisible) {
            // Passe du champ visible (TextField) au champ masqué (PasswordField)
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            toggleEye.setImage(new Image(getClass().getResourceAsStream("../ressources/img/oeil_ferme.png")));
            passwordVisible = false;
        } else {
            // Passe du champ masqué au champ visible
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            toggleEye.setImage(new Image(getClass().getResourceAsStream("../ressources/img/oeil_ouvert.png")));
            passwordVisible = true;
        }
    }

    /**
     * Attempts to authenticate the user using the provided email and password.
     * If authentication succeeds, redirects to the appropriate dashboard
     * based on user role (Admin or Secouriste).
     *
     * @param event the action event triggered by the login button
     * @throws IOException if the FXML view switch fails
     */
    @FXML
    private void handleConnexion(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();

        // === Tentative admin ===
        Admin admin = new AdminDAO().findByEmailAndPassword(email, password);
        if (admin != null) {
            GlobalController.setCurrentAdmin(admin);
            if (customCheckbox.isSelected()) saveEmail(email);
            else new File(FILE_PATH).delete();

            GlobalController.switchView("../ressources/fxml/TableauDeBordAdmin.fxml", (Node) event.getSource());
            return;
        }

        // === Tentative secouriste ===
        Secouriste s = new SecouristeDAO().findByEmail(email);
        try {
            if (s != null && s.getMotDePasse().equals(password)) {
                GlobalController.currentUser = s;
                GlobalController.switchView("../ressources/fxml/TableauDeBord.fxml", (Node) event.getSource());
            } else {
                showAlert("Email ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur d'authentification : " + e.getMessage());
        }

        // Mise à jour "se souvenir de moi"
        if (customCheckbox.isSelected()) saveEmail(email);
        else new File(FILE_PATH).delete();
    }

    /**
     * Saves the email locally if the "remember me" box is checked.
     *
     * @param email the email to persist in local storage
     */
    private void saveEmail(String email) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the saved email from the local file if it exists,
     * and updates the checkbox state accordingly.
     */
    private void loadRememberedEmail() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String savedEmail = reader.readLine();
                if (savedEmail != null) {
                    emailField.setText(savedEmail);
                    customCheckbox.setSelected(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Displays an error alert with the given message.
     *
     * @param message the content to show in the alert dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
