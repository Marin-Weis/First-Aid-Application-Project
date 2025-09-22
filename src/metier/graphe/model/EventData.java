package metier.graphe.model;

import javafx.scene.paint.Color;
import java.time.LocalTime;

/**
 * This lass represents an event with a label, start time, end time, and color.
 * It is used to store and manage event data in a calendar application.
 */
public class EventData {
    /** The label of the event. */
    private String label;
    /** The start time of the event. */
    private LocalTime start;
    /** The end time of the event. */
    private LocalTime end;
    /** The color associated with the event. */
    private Color color;

    /**
     * Constructs an EventData object with the specified label, start time, end time, and color.
     *
     * @param label The label of the event.
     * @param start The start time of the event.
     * @param end The end time of the event.
     * @param color The color associated with the event.
     */
    public EventData(String label, LocalTime start, LocalTime end, Color color) {
        this.label = label;
        this.start = start;
        this.end = end;
        this.color = color;
    }

    /**
     * Returns the label of the event.
     * @return the label of the event
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Returns the start time of the event.
     * @return the start time of the event
     */
    public LocalTime getStart() {
        return this.start;
    }

    /**
     * Returns the end time of the event.
     * @return the end time of the event
     */
    public LocalTime getEnd() {
        return this.end;
    }

    /**
     * Returns the color associated with the event.
     * @return the color of the event
     */
    public Color getColor() {
        return this.color;
    }

}