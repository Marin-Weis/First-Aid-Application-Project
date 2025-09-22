package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import metier.graphe.model.EventData;
import metier.graphe.model.dao.SecouristeDAO;
import metier.persistence.Admin;
import metier.persistence.Secouriste;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller for the admin dashboard view.
 * Displays a daily calendar with event visualization and handles navigation.
 * Allows dynamic visualization of events on an hourly grid.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 */
public class TableauDeBordAdminController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane scrollPaneCalendar;
    @FXML private Label timeLabel;
    @FXML private Label prenomLabel;
    @FXML private AnchorPane calendarPane;

    private final int startHour = 7;     // Heure de début du calendrier
    private final int endHour = 22;      // Heure de fin du calendrier
    private final int hourHeight = 60;   // Hauteur (en pixels) de chaque créneau horaire

    // Map qui associe une date à une liste d’événements
    private final Map<LocalDate, List<EventData>> eventMap = new HashMap<>();

    /**
     * Initializes the dashboard view with:
     * - Clock display
     * - Admin name display
     * - Scroll enhancements
     * - Sample events
     * - Daily event display
     *
     * @param location not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HeureController.afficherHeure(timeLabel);

        Admin admin = GlobalController.getCurrentAdmin();
        if (admin != null) {
            prenomLabel.setText(admin.getPrenom());
        }

        // Améliore la vitesse de scroll vertical
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        scrollPaneCalendar.setPannable(true);

        // Ajoute quelques événements de test pour aujourd’hui
        LocalDate today = LocalDate.now();
        addEvent(today, "DPS 1", LocalTime.of(9, 0), LocalTime.of(10, 0), Color.BLUE);
        addEvent(today, "DPS 2", LocalTime.of(12, 0), LocalTime.of(13, 0), Color.GREEN);

        // Affiche le calendrier du jour
        displayDay(today);
    }

    /**
     * Renders the hourly grid and events for a specific day in the calendar pane.
     *
     * @param day the date to display
     */
    private void displayDay(LocalDate day) {
        calendarPane.getChildren().clear();

        // Affiche chaque heure en texte et ligne horizontale
        for (int h = startHour; h <= endHour; h++) {
            Text hourText = new Text(10, (h - startHour) * hourHeight + 15, h + "h00");
            calendarPane.getChildren().add(hourText);

            Line hourLine = new Line(60, (h - startHour) * hourHeight, 363, (h - startHour) * hourHeight);
            hourLine.setStroke(Color.LIGHTGRAY);
            hourLine.setStrokeWidth(1);
            calendarPane.getChildren().add(hourLine);
        }

        // Réglage de la hauteur minimale de la grille
        double totalHeight = (endHour - startHour) * hourHeight + 200;
        calendarPane.setMinHeight(totalHeight);
        calendarPane.setPrefHeight(totalHeight);

        // Affiche tous les événements du jour
        List<EventData> events = eventMap.get(day);
        if (events != null) {
            for (EventData event : events) {
                createEvent(event.getLabel(), event.getStart(), event.getEnd(), event.getColor());
            }
        }
    }

    /**
     * Creates a colored rectangle and label for an event.
     *
     * @param label Name of the event
     * @param start Start time
     * @param end End time
     * @param color Event border color
     */
    private void createEvent(String label, LocalTime start, LocalTime end, Color color) {
        double startY = (start.getHour() + start.getMinute() / 60.0 - startHour) * hourHeight;
        double height = ((end.toSecondOfDay() - start.toSecondOfDay()) / 3600.0) * hourHeight;

        Rectangle rect = new Rectangle(100, startY, 200, height);
        rect.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.2));
        rect.setStroke(color);

        VBox textContainer = new VBox();
        textContainer.setLayoutX(100);
        textContainer.setLayoutY(startY);
        textContainer.setPrefSize(200, height);
        textContainer.setAlignment(Pos.CENTER);

        Label labelText = new Label(start + " - " + end + "\n" + label);
        labelText.setStyle("-fx-text-fill: black;");
        labelText.setWrapText(true);
        labelText.setTextAlignment(TextAlignment.CENTER);
        labelText.setAlignment(Pos.CENTER);
        labelText.setMaxWidth(195);

        textContainer.getChildren().add(labelText);
        calendarPane.getChildren().addAll(rect, textContainer);
    }

    /**
     * Adds a new event to the internal event map for a given date.
     *
     * @param day Target date
     * @param label Label for the event
     * @param start Start time
     * @param end End time
     * @param color Color used for rendering
     */
    private void addEvent(LocalDate day, String label, LocalTime start, LocalTime end, Color color) {
        List<EventData> events = eventMap.computeIfAbsent(day, d -> new ArrayList<>());

        // Vérifie les conflits
        for (EventData event : events) {
            if (start.isBefore(event.getEnd()) && end.isAfter(event.getStart())) {
                System.out.println("L'événement chevauche un autre. Ignoré.");
                return;
            }
        }

        events.add(new EventData(label, start, end, color));
        System.out.println("Événement ajouté : " + label + " de " + start + " à " + end);
    }

    // ==== MÉTHODES DE NAVIGATION ENTRE LES VUES ====

    /**
     * Navigates to the admin profile view.
     * @param event the triggering mouse event
     */
    @FXML
    private void handleProfilAdmin(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur vue ProfilAdmin : " + e.getMessage());
        }
    }


    /**
     * Navigates to the weekly admin calendar.
     * @param event the triggering mouse event
     */
    @FXML
    private void handleCalendrierAdmin(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierAdminSemaine.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur vue CalendrierAdminSemaine : " + e.getMessage());
        }
    }


    /**
     * Navigates to the admin alerts view.
     * @param event the triggering mouse event
     */
    @FXML
    private void handleAlertesAdmin(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/AlertesAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur vue AlertesAdmin : " + e.getMessage());
        }
    }


    /**
     * Navigates to the list of all rescuers.
     * @param event the triggering mouse event
     */
    @FXML
    private void handleEffectif(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ListeDesSecouristesAdmin.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur vue ListeDesSecouristesAdmin : " + e.getMessage());
        }
    }


    /**
     * Navigates to the DPS calendar creation view.
     * @param event the triggering action event
     */
    @FXML
    private void handleCreateDPS(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierAdminMois.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur vue CalendrierAdminMois : " + e.getMessage());
        }
    }


    /**
     * Handles exporting the rescuers’ data as a CSV file.
     * Opens a file dialog and writes data using the ExportCSV utility.
     *
     * @param event the triggering action event
     */
    @FXML
    private void handleExportCSV(ActionEvent event) {
        // Création d'une instance du DAO pour accéder aux données des secouristes
        SecouristeDAO dao = new SecouristeDAO();

        // Appel de la méthode findAll() pour récupérer tous les secouristes depuis la base de données
        List<Secouriste> liste = dao.findAll();

        // Création d'une fenêtre pour choisir l'emplacement et le nom du fichier à enregistrer
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer sous..."); // Titre de la fenêtre
        fileChooser.setInitialFileName("secouristes.csv"); // Nom proposé par défaut

        // Filtre pour ne permettre que les fichiers .csv
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );

        // Affiche la boîte de dialogue de sauvegarde et récupère le fichier sélectionné
        File fichier = fileChooser.showSaveDialog(null);

        // Si l'utilisateur a bien sélectionné un fichier (et n'a pas annulé)
        if (fichier != null) {
            // Appel de la méthode utilitaire pour écrire les données dans le fichier CSV
            ExportCSV.exportSecouristes(liste, fichier.getAbsolutePath());
        }
    }

}
