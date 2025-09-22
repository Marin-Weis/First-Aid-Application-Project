// CalendrierSecouristeMoisController.java
package controller;

// Importations nécessaires à JavaFX et au traitement des dates
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import metier.graphe.model.dao.DispoDAO;
import metier.persistence.Disponibilite;
import metier.persistence.Journee;
import metier.persistence.Secouriste;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller class for the monthly calendar view used by rescuers.
 * Displays the current month, allows month navigation, and shows a grid of days.
 * Includes date pickers for availability management and supports a popup pane.
 */
public class CalendrierSecouristeMoisController implements Initializable {

    // Label affichant l'heure dans la barre supérieure
    @FXML private Label timeLabel;

    // HBox contenant les boutons représentant les mois
    @FXML private HBox moisSelector;

    // Label du nom du mois affiché sous forme complète (ex: Juin)
    @FXML private Label labelMoisActuel;

    // Grille affichant les jours du mois sélectionné
    @FXML private GridPane gridMois;

    // Référence au bouton actuellement sélectionné pour gérer le style
    private Button boutonSelectionne = null;

    @FXML private VBox popupPane;
    @FXML private Rectangle overlay;

    @FXML private DatePicker datePickerStart;
    @FXML private DatePicker datePickerEnd;

    @FXML private ComboBox<String> hourComboBoxStart;
    @FXML private ComboBox<String> hourComboBoxEnd;

    @FXML private Button validerButton; // si besoin

    /**
     * Initializes the calendar interface, displays the current time and month,
     * sets up the month selector buttons, and fills the grid with the current month's days.
     * @param url - not used
     * @param resourceBundle - not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Affichage dynamique de l'heure dans la barre supérieure
        HeureController.afficherHeure(timeLabel);

        // Récupération de la date du jour
        LocalDate today = LocalDate.now();
        String moisEnLettres = today.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH);
        labelMoisActuel.setText(moisEnLettres.substring(0, 1).toUpperCase() + moisEnLettres.substring(1));

        // Numéro du mois actuel
        int currentMonth = today.getMonthValue();

        // Création des boutons de chaque mois
        for (int i = 1; i <= 12; i++) {
            Button bouton = createMonthButton(i, currentMonth, today.getDayOfMonth());
            final int selectedMonth = i;

            // Action lors du clic sur un bouton de mois
            bouton.setOnAction(e -> {
                afficherMois(selectedMonth);           // Met à jour le calendrier
                mettreAJourSelection(bouton);          // Met à jour le style sélectionné
            });

            // Sauvegarde le bouton du mois actuel comme sélectionné au démarrage
            if (i == currentMonth) {
                boutonSelectionne = bouton;
            }

            // Ajoute le bouton à l'interface
            moisSelector.getChildren().add(bouton);
        }

        // Affiche les jours du mois courant au chargement
        afficherMois(currentMonth);
    }

    /**
     * Creates a month button with appropriate styling depending on whether it is
     * the currently selected month or not. Displays the abbreviated name and number of the month.
     * @param month - the month number (1 to 12)
     * @param currentMonth - the current system month
     * @param currentDay - the current system day
     * @return a styled Button representing the month
     */
    public Button createMonthButton(int month, int currentMonth, int currentDay) {
        VBox vbox = new VBox(); // Conteneur vertical pour afficher le mois + chiffre
        Button btn = new Button();

        if (month == currentMonth) {
            // Style pour le mois sélectionné (rouge)
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: #E60023; -fx-background-radius: 10;");
            vbox.setPrefSize(41, 50);
            vbox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setSpacing(0);

            // Nom abrégé + numéro du mois
            Label monthLabel = new Label(getMonthAbbr(month));
            monthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10.49;");

            Label numLabel = new Label(String.format("%02d", month));
            numLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10.49;");

            vbox.getChildren().addAll(monthLabel, numLabel);

        } else {
            // Style pour les autres mois (gris clair)
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: rgba(171, 171, 171, 0.51); -fx-background-radius: 10;");
            vbox.setPrefSize(41, 50);
            vbox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setSpacing(0);

            Label monthLabel = new Label(getMonthAbbr(month));
            monthLabel.setStyle("-fx-text-fill: black; -fx-font-size: 10.49;");

            Label numLabel = new Label(String.format("%02d", month));
            numLabel.setStyle("-fx-text-fill: black; -fx-font-size: 10.49;");

            vbox.getChildren().addAll(monthLabel, numLabel);
        }

        // Bouton sans fond ni bord, contenant le VBox
        btn.setGraphic(vbox);
        btn.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: transparent;");
        btn.setPrefSize(41, 50);
        btn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        btn.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        return btn;
    }

    /**
     * Returns the abbreviated French name of a given month.
     * @param month - the month number (1 to 12)
     * @return the abbreviated French month name (e.g., janv., févr.)
     */
    private String getMonthAbbr(int month) {
        return LocalDate.of(2000, month, 1)
                .getMonth()
                .getDisplayName(java.time.format.TextStyle.SHORT, Locale.FRENCH);
    }

    /**
     * Updates the calendar grid with the days of the selected month.
     * Also updates the label to show the full name of the selected month.
     * @param month - the month to display (1 to 12)
     */
    private void afficherMois(int month) {
        gridMois.getChildren().clear();

        // Mettre à jour le label avec le nom du mois sélectionné
        String nomMois = LocalDate.of(2000, month, 1)
                .getMonth()
                .getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH);
        labelMoisActuel.setText(nomMois.substring(0, 1).toUpperCase() + nomMois.substring(1));

        YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
        LocalDate firstOfMonth = yearMonth.atDay(1);
        DayOfWeek dayOfWeek = firstOfMonth.getDayOfWeek();
        int offset = (dayOfWeek.getValue() + 6) % 7;
        int lengthOfMonth = yearMonth.lengthOfMonth();

        int jour = 1;
        for (int ligne = 0; ligne < 6; ligne++) {
            for (int colonne = 0; colonne < 7; colonne++) {
                if (ligne == 0 && colonne < offset) {
                    gridMois.add(new Label(""), colonne, ligne);
                } else if (jour <= lengthOfMonth) {
                    Label labelJour = new Label(String.valueOf(jour));
                    labelJour.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: black;");
                    labelJour.setAlignment(Pos.CENTER);
                    labelJour.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setFillWidth(labelJour, true);
                    gridMois.add(labelJour, colonne, ligne);
                    jour++;
                }
            }
        }
    }

    /**
     * Updates the visual style of the selected month button by highlighting
     * the newly selected one in red and resetting the previous selection to gray.
     * @param boutonClique - the Button that was clicked
     */
    private void mettreAJourSelection(Button boutonClique) {
        if (boutonSelectionne != null) {
            // Réinitialise l'ancien bouton (remet en gris)
            VBox ancienContenu = (VBox) boutonSelectionne.getGraphic();
            ancienContenu.setStyle("-fx-background-color: rgba(171, 171, 171, 0.51); -fx-background-radius: 10;");
            for (Node node : ancienContenu.getChildren()) {
                if (node instanceof Label) {
                    ((Label) node).setStyle("-fx-text-fill: black; -fx-font-size: 10.49;");
                }
            }
        }

        // Applique le style rouge au bouton sélectionné
        VBox nouveauContenu = (VBox) boutonClique.getGraphic();
        nouveauContenu.setStyle("-fx-background-color: #E60023; -fx-background-radius: 10;");
        for (Node node : nouveauContenu.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setStyle("-fx-text-fill: white; -fx-font-size: 10.49;");
            }
        }

        boutonSelectionne = boutonClique;
    }

    @FXML
    /**
     * This method is called when the "Accueil" button is clicked.
     * It loads the TableauDeBord.fxml and sets it as the new scene with rounded corners and transparency.
     *
     * @param event The MouseEvent triggered by the button click.
     */
    private void handleAccueil(MouseEvent event) {
        try {
            // On récupère la scène actuelle à partir de l'élément source de l'événement
            // event.getSource() est le bouton qui a été cliqué (la source)
            GlobalController.switchView("../ressources/fxml/TableauDeBord.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue TableauDeBord : " + e.getMessage());
        }
    }

    @FXML
    /**
     * This method is called when the "Alertes" button is clicked.
     * It loads the NotificationSecouriste.fxml and sets it as the new scene with rounded corners and transparency.
     *
     * @param event The MouseEvent triggered by the button click.
     */
    private void handleAlertes(MouseEvent event) throws IOException {
        try {
            GlobalController.switchView("../ressources/fxml/NotificationSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue NotificationSecouriste : " + e.getMessage());
        }

    }

    @FXML
    /**
     * This method is called when the "Retour" button is clicked.
     * It loads the CalendrierSecouristeSemaine.fxml and sets it as the new scene with rounded corners and transparency.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    private void handleRetourSemaine(MouseEvent event) {
        try {
            // Redirige vers la vue CalendrierSecouristeSemaine.fxml
            GlobalController.switchView("../ressources/fxml/CalendrierSecouristeSemaine.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue CalendrierSecouristeSemaine : " + e.getMessage());
        }
    }

    /**
     * Displays the popup window for setting an unavailability period,
     * and shows the background overlay.
     */
    @FXML
    private void showPopup() {
        popupPane.setVisible(true);
        overlay.setVisible(true);
    }

    /**
     * Hides the popup window and removes the background overlay.
     */
    @FXML
    private void hidePopup() {
        popupPane.setVisible(false);
        overlay.setVisible(false);
    }

    /**
     * Handles the validation and saving of an unavailability period for the current rescuer.
     * Collects start and end dates and times, verifies validity, and stores one unavailability
     * entry per day in the database. Displays an error message if the time interval is invalid
     * or if the database operation fails.
     * @param event - the ActionEvent triggered by the user's validation action
     */
    @FXML
    private void handleValiderIndisponibilite(ActionEvent event) {
        try {
            LocalDate dateDebut = datePickerStart.getValue();
            LocalDate dateFin = datePickerEnd.getValue();

            String heureDebutStr = hourComboBoxStart.getValue().replace("h", "");
            String heureFinStr = hourComboBoxEnd.getValue().replace("h", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

            LocalTime heureDebut = LocalTime.parse(heureDebutStr, formatter);
            LocalTime heureFin = LocalTime.parse(heureFinStr, formatter);

            LocalDateTime debut = LocalDateTime.of(dateDebut, heureDebut);
            LocalDateTime fin = LocalDateTime.of(dateFin, heureFin);

            if (debut.isAfter(fin)) {
                System.out.println("Erreur : L'heure de début est après l'heure de fin.");
                return;
            }

            Secouriste secouriste = GlobalController.getCurrentUser();
            long idSecouristeConnecte = secouriste.getId();

            DispoDAO dispoDAO = new DispoDAO();

            LocalDate current = dateDebut;
            while (!current.isAfter(dateFin)) {
                Journee jour = new Journee(current.getDayOfMonth(), current.getMonthValue(), current.getYear());
                Disponibilite indispo = new Disponibilite(idSecouristeConnecte, jour);

                int result = dispoDAO.create(indispo);
                if (result <= 0) {
                    System.err.println("Erreur lors de l'enregistrement de l'indisponibilité pour le " + jour);
                }

                current = current.plusDays(1);
            }

            System.out.println("Indisponibilité signalée de " + debut + " à " + fin);

            hidePopup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
