package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import metier.graphe.model.EventData;
import metier.graphe.model.dao.DPSDAO;
import metier.persistence.DPS;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TableauDeBordController {

    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane scrollPaneCalendar;
    @FXML private Label prenomLabel;
    @FXML private Label timeLabel;
    @FXML private AnchorPane calendarPane;

    private final int startHour = 7;
    private final int endHour = 22;
    private final int hourHeight = 60;

    private final Map<LocalDate, List<EventData>> eventMap = new HashMap<>();

    /**
     * Initializes the dashboard for the connected secouriste.
     * Displays today's events and sets up scroll behavior.
     */
    public void initialize() {
        LocalDate today = LocalDate.now();

        // Récupère les événements (DPS) liés à l'utilisateur connecté
        long curentUser = GlobalController.getCurrentUser().getId();
        DPSDAO dao = new DPSDAO();
        List<DPS> affectes = dao.findBySecouriste(curentUser);

        for (DPS dps : affectes) {
            LocalDate date = dps.getDate().toLocalDate();
            LocalTime debut = dps.getHeureDebut().toLocalTime();
            LocalTime fin = dps.getHeureFin().toLocalTime();
            String label = dps.getLabel();
            String couleurTexte = dps.getCouleur();
            Color couleur = Color.valueOf(couleurTexte != null ? couleurTexte : "ORANGE");

            EventData event = new EventData(label, debut, fin, couleur);
            eventMap.computeIfAbsent(date, d -> new ArrayList<>()).add(event);
        }

        displayDay(today);
        HeureController.afficherHeure(this.timeLabel);
        prenomLabel.setText(GlobalController.currentUser.getPrenom());

        // Accélère le scroll vertical
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        scrollPaneCalendar.setPannable(true);
    }

    /**
     * Displays the events and hour lines for the selected day.
     *
     * @param day The date to display.
     */
    private void displayDay(LocalDate day) {
        calendarPane.getChildren().clear();

        // Affiche les lignes d'heures
        for (int h = startHour; h <= endHour; h++) {
            Text hourText = new Text(10, (h - startHour) * hourHeight + 15, h + "h00");
            calendarPane.getChildren().add(hourText);

            Line hourLine = new Line(60, (h - startHour) * hourHeight, 363, (h - startHour) * hourHeight);
            hourLine.setStroke(Color.LIGHTGRAY);
            hourLine.setStrokeWidth(1);
            calendarPane.getChildren().add(hourLine);
        }

        // Ajuste la taille verticale du calendrier
        double totalHeight = (endHour - startHour) * hourHeight + 200;
        calendarPane.setMinHeight(totalHeight);
        calendarPane.setPrefHeight(totalHeight);

        // Ajoute les événements de la journée
        List<EventData> events = eventMap.get(day);
        if (events != null) {
            for (EventData event : events) {
                createEvent(event.getLabel(), event.getStart(), event.getEnd(), event.getColor());
            }
        }
    }

    /**
     * Creates a visual event block on the calendar for the given time slot.
     *
     * @param label Label of the event.
     * @param start Start time.
     * @param end End time.
     * @param color Color of the event border and background.
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
     * Navigates to the weekly calendar view.
     *
     * @param event Click event from the calendar icon.
     */
    @FXML
    private void handleCalendrierSecouriste(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierSecouristeSemaine.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue CalendrierSecouriste : " + e.getMessage());
        }
    }

    /**
     * Logs out the user and returns to the welcome view.
     *
     * @param event Click event from the logout button.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            GlobalController.currentUser = null;
            GlobalController.switchView("../ressources/fxml/Accueil.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue Accueil : " + e.getMessage());
        }
    }

    /**
     * Opens the notification view for secouristes.
     *
     * @param event Click event from the bell icon.
     */
    @FXML
    private void handleAlertes(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/NotificationSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue NotificationSecouriste : " + e.getMessage());
        }
    }

    /**
     * Opens the profile view for the connected secouriste.
     *
     * @param event Click event from the profile icon.
     */
    @FXML
    private void handleProfilClick(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/ProfilSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue ProfilSecouriste : " + e.getMessage());
        }
    }
}
