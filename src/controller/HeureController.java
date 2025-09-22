package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for managing and displaying the current time dynamically in a JavaFX Label.
 * Updates every second using a Timeline animation.
 *
 * This controller is typically used to display a live clock in the UI (e.g., dashboard header).
 *
 * Authors: M. Weis, N. Lescop, M. Gouelo, A. Jan
 * Version: 1.0
 */
public class HeureController {

    /**
     * Continuously updates the provided Label with the current time in HH:mm format.
     * The update occurs every second using a Timeline.
     *
     * @param timeLabel the JavaFX Label to be updated with the current time
     */
    public static void afficherHeure(Label timeLabel) {
        // Vérifie que le label n'est pas null (évite les erreurs à l'initialisation)
        if (timeLabel == null) {
            return;
        }

        // Affiche immédiatement l’heure courante au démarrage
        LocalTime currentTime = LocalTime.now();
        timeLabel.setText(currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        // Crée une Timeline qui met à jour le label toutes les secondes
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalTime now = LocalTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
        }));

        // La Timeline tourne indéfiniment
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
