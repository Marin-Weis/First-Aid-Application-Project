package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import metier.graphe.model.EventData;
import metier.graphe.model.dao.DPSDAO;
import metier.persistence.DPS;
import metier.service.PlanningMngtSec;
import java.io.IOException;
import java.util.*;

import metier.graphe.model.dao.DPSDAO;

/**
 * This class allows to manage the calendar view for rescuers.
 * It provides a method to switch to the calendar view when the button is clicked.
 */
public class CalendrierSecouristeSemaineController {

    @FXML
    /**
     * This label is used to display the current time in the application.
     * It is updated every second to show the current time in the format "HH:mm:ss".
     */
    private Label timeLabel;

    @FXML
    /**
     * This HBox is used to hold the buttons for selecting days of the week.
     * It is defined in the FXML file and is used to display the buttons for each day.
     */
    private HBox daySelector;

    @FXML
    /**
     * This AnchorPane is used to display the calendar for the selected week.
     * It is defined in the FXML file and is used to show the schedule for each day.
     */
    private AnchorPane calendarPane;

    /** This attributes define the start hours of the calendar display */
    private final int startHour = 7;

    /** This attributes define the end hours of the calendar display */
    private final int endHour = 22;

    /** This attributes define the height of each hour slot in pixels */
    private final int hourHeight = 60; // 60 pixels par heure

    /**
     * This list holds the events for the current day.
     * Each event is represented by a LocalTime array containing the start and end times.
     */
    private final Map<LocalDate, List<EventData>> eventMap = new HashMap<>();

    /**
     * This method is called to display the current time in the specified label.
     */
    public void initialize() {

        // Afficher les jours (par ex : Lun 24, Mar 25...)
        LocalDate today = LocalDate.now(); // Charge la date du jour

        // Calcule le début de la semaine en faisant le calcul à partir du jour actuel
        // Exemple : Si aujourd'hui est Mercredi, on va afficher les jours de Lundi à Dimanche
        // -1 car getDayOfWeek().getValue() retourne 1 pour Lundi, 2 pour Mardi, etc.
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // .minusday permet de retirer le nombre de jour

        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            Button bouton;
            bouton = createDayButton(day, today);
            bouton.setOnAction(e -> {
                displayDay(day);
                if (!day.equals(today)) {
                    timeLabel.setText(day.format(DateTimeFormatter.ofPattern("EEEE", Locale.FRENCH)) + " " + day.getDayOfMonth() + " " + day.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH));
                } else {
                    timeLabel.setText("Aujourd'hui");
                }
            });
            daySelector.getChildren().add(bouton);
        }

        System.out.println(LocalDate.of(2025, 6, 18));
        System.out.println(LocalTime.of(7, 0));


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
    }

    /**
     * This method creates a button for a specific day with the given date.
     * It styles the button based on whether the date is today or not.
     *
     * @param date The date for which the button is created.
     * @param today The current date to compare against.
     * @return A Button styled for the specified day.
     */
    private Button createDayButton(LocalDate date, LocalDate today) {
        // Clone la structure en pur Java
        VBox vbox = new VBox();
        Button btn = new Button();

        System.out.println("la date : " + date + " aujourd'hui : " + today);
        if (today.equals(date)) {
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: #E60023; -fx-background-radius: 10;");
            vbox.setPrefSize(41, 50);
            vbox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setSpacing(0);

            Label dayLabel = new Label();

            // Créer un formateur de date en français
            DateTimeFormatter frenchDayFormatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);

            // Appliquer au label
            String jourAbrege = date.format(frenchDayFormatter); // "lun.", "mar.", etc.
            dayLabel.setText(jourAbrege.substring(0, 3).toUpperCase()); // "LUN", "MAR", etc.
            dayLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10.49;");

            Label numLabel = new Label(String.valueOf(date.getDayOfMonth()));
            numLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10.49;");

            vbox.getChildren().addAll(dayLabel, numLabel);

            btn.setGraphic(vbox);
            btn.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: transparent;");
            btn.setPrefSize(41, 50);
            btn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            btn.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        } else {
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: rgba(171, 171, 171, 0.51); -fx-background-radius: 10;");
            vbox.setPrefSize(41, 50);
            vbox.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            vbox.setSpacing(0);

            Label dayLabel = new Label();

            // Créer un formateur de date en français
            DateTimeFormatter frenchDayFormatter = DateTimeFormatter.ofPattern("E", Locale.FRENCH);

            // Appliquer au label
            String jourAbrege = date.format(frenchDayFormatter); // "lun.", "mar.", etc.
            dayLabel.setText(jourAbrege.substring(0, 3).toUpperCase()); // "LUN", "MAR", etc.
            dayLabel.setStyle("-fx-text-fill: black; -fx-font-size: 10.49;");

            Label numLabel = new Label(String.valueOf(date.getDayOfMonth()));
            numLabel.setStyle("-fx-text-fill: black; -fx-font-size: 10.49;");

            vbox.getChildren().addAll(dayLabel, numLabel);

            btn.setGraphic(vbox);
            btn.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: transparent;");
            btn.setPrefSize(41, 50);
            btn.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            btn.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        }

        return btn;
    }

    /**
     * This method displays the calendar for the specified day.
     * It clears the current calendar pane and populates it with the schedule for the given day.
     *
     * @param day The day to display in the calendar.
     */
    private void displayDay(LocalDate day) {
        calendarPane.getChildren().clear();

        // Affiche les lignes horaires
        for (int h = startHour; h <= endHour; h++) {

            // 10 : 10 pixels depuis la gauche
            // (h - startHour) : Calcul du numéro d'heure relatif
            // * hourHeight : Conversion en pixels (hauteur d'un créneau horaire)
            // + 15 : Décalage vertical supplémentaire
            // h + "h00" : Formatage du texte de l'heure
            Text hourText = new Text(10, (h - startHour) * hourHeight + 15, h + "h00");
            calendarPane.getChildren().add(hourText);

            // Ligne horizontale pour chaque heure
            Line hourLine = new Line();
            hourLine.setStartX(60); // Position de début sur l'axe X (après le texte de l'heure)
            hourLine.setEndX(1000); // Largeur du planning
            hourLine.setStartY((h - startHour) * hourHeight);
            hourLine.setEndY((h - startHour) * hourHeight);
            hourLine.setStroke(Color.LIGHTGRAY); // Couleur des lignes
            hourLine.setStrokeWidth(1); // Épaisseur fine

            calendarPane.getChildren().add(hourLine);
        }

        // Ajustement de la hauteur minimale pour le ScrollPane
        double totalHeight = (endHour - startHour) * hourHeight + 200; // Heures + espace
        calendarPane.setMinHeight(totalHeight);

        List<EventData> events = eventMap.get(day);
        if (events != null) {
            for (EventData event : events) {
                createEvent(event.getLabel(), event.getStart(), event.getEnd(), event.getColor());
            }
        }
    }

    /**
     * This method creates an event rectangle on the calendar pane.
     * It calculates the position and height based on the start and end times of the event.
     *
     * @param label The label for the event.
     * @param start The start time of the event.
     * @param end The end time of the event.
     * @param color The color of the event rectangle border.
     */
    private void createEvent(String label, LocalTime start, LocalTime end, Color color) {
        // Calcul de la position verticale (Y) du début de l'événement
        double startY = (start.getHour() + (start.getMinute() / 60.0) - startHour) * hourHeight;
        // Explication :
        // 1. start.getHour() : heure de début (ex: 14 pour 14h25)
        // 2. start.getMinute() / 60.0 : conversion des minutes en fraction d'heure (ex: 25/60 = 0.416)
        // 3. - startHour : soustrait l'heure de début de l'affichage (ex: -8 si le planning commence à 8h)
        // 4. * hourHeight : convertit en pixels (ex: si hourHeight=60px/h, (14.416-8)*60 = 385px)

        // Calcul de la hauteur de l'événement en pixels
        double height = ((end.toSecondOfDay() - start.toSecondOfDay()) / 3600.0) * hourHeight;
        // Explication :
        // 1. end.toSecondOfDay() - start.toSecondOfDay() : durée totale en secondes
        // 2. / 3600.0 : conversion secondes → heures (3600s = 1h)
        // 3. * hourHeight : conversion en pixels selon l'échelle d'affichage
        // Exemple pour 14h30-15h45 :
        // (56700 - 52200) = 4500 secondes (1h15)
        // 4500/3600 = 1.25 heures
        // 1.25 * 60px/h = 75px de hauteur

        // 100 : Position X (100px depuis la gauche)
        // startY : Position Y calculée dynamiquement
        // 200 : Largeur fixe (200px)
        // height : Hauteur calculée en fonction de la durée
        Rectangle rect = new Rectangle(100, startY, 200, height);
        rect.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.2));
        rect.setStroke(color);

        VBox textContainer = new VBox();
        // Même dimension que rect
        textContainer.setLayoutX(100);
        textContainer.setLayoutY(startY);
        textContainer.setPrefSize(200, height);
        textContainer.setAlignment(Pos.CENTER);

        Label labelText = new Label(start + " - " + end + "\n" + label);
        labelText.setStyle("-fx-text-fill: black;");
        labelText.setWrapText(true); // Permet de revenir à la ligne
        labelText.setTextAlignment(TextAlignment.CENTER); // Centre le texte à l'intérieur du Label
        labelText.setAlignment(Pos.CENTER); // Centre dans la VBox
        labelText.setMaxWidth(195); // Limite la largeur pour forcer les retours à la ligne

        textContainer.getChildren().add(labelText);
        calendarPane.getChildren().addAll(rect, textContainer);

    }

    /**
     * This method adds an event to the event map for a specific day.
     * It creates a new EventData object and adds it to the list of events for that day.
     *
     * @param day The day for which the event is being added.
     * @param label The label for the event.
     * @param start The start time of the event.
     * @param end The end time of the event.
     * @param color The color of the event.
     */
    public void addEvent(LocalDate day, String label, LocalTime start, LocalTime end, Color color) {
        // Création d'une liste d'événements pour le jour spécifié
        List<EventData> events = eventMap.computeIfAbsent(day, d -> new ArrayList<>());

        // Vérifie si l'événement chevauche un événement existant
        for (EventData event : events) {
            if (start.isBefore(event.getEnd()) && end.isAfter(event.getStart())) {
                System.out.println("L'événement chevauche un événement existant, il ne sera pas ajouté.");
                return; // On arrête l'exécution de la méthode
            }
        }

        // Si l'événement n'existe pas, on le crée et l'ajoute à la liste
        EventData newEvent = new EventData(label, start, end, color);
        events.add(newEvent);

        PlanningMngtSec ajout = new PlanningMngtSec();
        ajout.addEvent(day, newEvent);

        System.out.println("Événement ajouté : " + label + " de " + start + " à " + end);

    }

    @FXML
    /**
     * This method is called when the back button is clicked.
     * It loads the TableauDeBord.fxml and sets it as the new scene with rounded corners and transparency.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the FXML file.
     */
    private void handleTableauDeBord(MouseEvent event) {
        // On récupère la scène actuelle à partir de l'élément source de l'événement
        // event.getSource() est le bouton qui a été cliqué (la source)
        try {
            GlobalController.switchView("../ressources/fxml/TableauDeBord.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue TableauDeBord : " + e.getMessage());
        }
    }

    @FXML
    /**
     * This method is called when the back button is clicked.
     * It loads the NotificationSecouriste.fxml and sets it as the new scene with rounded corners and transparency.
     *
     * @param event The ActionEvent triggered by the button click.
     * @throws IOException If there is an error loading the FXML file.
     */
    private void handleAlertes(MouseEvent event) {
        // On récupère la scène actuelle à partir de l'élément source de l'événement
        // event.getSource() est le bouton qui a été cliqué (la source)
        try {
            GlobalController.switchView("../ressources/fxml/NotificationSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la vue NotificationSecouriste : " + e.getMessage());
        }
    }

//    public AnchorPane getCalendarDayNow(LocalDate day) {
//        AnchorPane calendar = new AnchorPane();
//        double totalHeight = (endHour - startHour) * hourHeight + 200; // Heures + espace
//        calendar.setMinHeight(totalHeight);
//
//        // Création d'une liste d'événements pour le jour spécifié
//        List<EventData> events = this.eventMap.get(day);
//
//        if (events != null) {
//            for (EventData event : events) {
//                // Calcul de la position verticale (Y) du début de l'événement
//                double startY = (event.getStart().getHour() + (event.getStart().getMinute() / 60.0) - startHour) * hourHeight;
//                // Calcul de la hauteur de l'événement en pixels
//                double height = ((event.getEnd().toSecondOfDay() - event.getStart().toSecondOfDay()) / 3600.0) * hourHeight;
//
//                Rectangle rect = new Rectangle(100, startY, 200, height);
//                rect.setFill(new Color(event.getColor().getRed(), event.getColor().getGreen(), event.getColor().getBlue(), 0.2));
//                rect.setStroke(event.getColor());
//
//                VBox textContainer = new VBox();
//                // Même dimension que rect
//                textContainer.setLayoutX(100);
//                textContainer.setLayoutY(startY);
//                textContainer.setPrefSize(200, height);
//                textContainer.setAlignment(Pos.CENTER);
//
//                Label labelText = new Label(event.getStart() + " - " + event.getEnd() + "\n" + event.getLabel());
//                labelText.setStyle("-fx-text-fill: black;");
//                labelText.setWrapText(true); // Permet de revenir à la ligne
//                labelText.setTextAlignment(TextAlignment.CENTER); // Centre le texte à l'intérieur du Label
//                labelText.setAlignment(Pos.CENTER); // Centre dans la VBox
//                labelText.setMaxWidth(195); // Limite la largeur pour forcer les retours à la ligne
//
//                textContainer.getChildren().add(labelText);
//                calendar.getChildren().addAll(rect, textContainer);
//            }
//        } else {
//            System.err.println("La lsite d'évènement est vide pour la méthode getCalendarDayNow");
//        }
//        return calendar;
//    }

    /**
     * Handles returning to the monthly calendar view for the rescuer interface.
     * Switches the scene to the 'CalendrierSecouristeMois' view.
     * @param event - the MouseEvent triggered by the user's click
     */
    @FXML
    private void handleRetourMois(MouseEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierSecouristeMois.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du retour au calendrier mois : " + e.getMessage());
        }
    }
}