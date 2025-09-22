/**
 * Global controller managing navigation between views across the entire application.
 * It supports animated transitions, view history tracking, and current user/admin/session state.
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import metier.persistence.Admin;
import metier.persistence.Secouriste;

import java.io.IOException;
import java.util.Stack;

public class GlobalController {

    /** The currently connected Secouriste user (if any). */
    public static Secouriste currentUser;

    /** The currently connected Admin user (if any). */
    private static Admin currentAdmin;

    /** Stores the Secouriste currently being consulted (for admin purposes). */
    private static Secouriste selectedSecouriste;

    /**
     * Stack that holds the view history for navigation purposes.
     * Used to return to previous scenes when needed.
     */
    private static final Stack<Parent> viewHistory = new Stack<>();

    /**
     * Switches the current view to a new one using the specified FXML path.
     * Performs an animated transition from right to left.
     *
     * @param fxmlPath the path of the FXML file to load (relative to resource folder)
     * @param source the node that triggered the scene change (used to get the current scene)
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void switchView(String fxmlPath, Node source) throws IOException {
        Scene scene = source.getScene();

        if (scene != null) {
            // Sauvegarde la vue actuelle pour pouvoir revenir en arrière
            Parent currentView = scene.getRoot();
            viewHistory.push(currentView);

            // Charge la nouvelle vue à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(GlobalController.class.getResource(fxmlPath));
            Parent newView = loader.load();

            // Préparation de la transition (slide-in depuis la droite)
            newView.translateXProperty().set(scene.getWidth());
            scene.setRoot(newView);

            TranslateTransition transition = new TranslateTransition(Duration.millis(300), newView);
            transition.setFromX(scene.getWidth());
            transition.setToX(0);
            transition.play();

        } else {
            System.err.println("Erreur : le Node source n'est pas attaché à une scène.");
        }
    }

    /**
     * Switches to the Secouriste profile view, storing the current view in history.
     *
     * @param event the action event triggered by a UI component
     */
    public void goToProfil(ActionEvent event) {
        try {
            // Sauvegarde la vue actuelle dans l'historique
            Parent currentView = ((Node) event.getSource()).getScene().getRoot();
            viewHistory.push(currentView);

            // Chargement du profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfilSecouriste.fxml"));
            Parent profilView = loader.load();

            // Affichage de la nouvelle vue
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(profilView);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue ProfilSecouriste : " + e.getMessage());
        }
    }

    /**
     * Returns the history stack of previously visited views.
     * Can be used for back navigation logic.
     *
     * @return the stack of Parent views
     */
    public static Stack<Parent> getViewHistory() {
        return viewHistory;
    }

    /**
     * Returns the currently logged-in Secouriste.
     *
     * @return the current Secouriste, or null if none
     */
    public static Secouriste getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the currently logged-in Admin.
     *
     * @param admin the admin user to set as current
     */
    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    /**
     * Returns the currently logged-in Admin.
     *
     * @return the current Admin, or null if none
     */
    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    /**
     * Sets the selected Secouriste (used for detail views).
     *
     * @param s the Secouriste to set
     */
    public static void setSelectedSecouriste(Secouriste s) {
        selectedSecouriste = s;
    }

    /**
     * Returns the Secouriste selected for viewing (used in admin features).
     *
     * @return the selected Secouriste
     */
    public static Secouriste getSelectedSecouriste() {
        return selectedSecouriste;
    }
}
