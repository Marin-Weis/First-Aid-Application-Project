package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import metier.graphe.model.dao.PossedeDAO;
import metier.graphe.model.dao.SecouristeDAO;
import metier.persistence.Competences;
import metier.persistence.Possede;
import metier.persistence.Secouriste;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la vue de profil d’un secouriste (côté admin).
 * Permet d'afficher les informations personnelles, gérer les compétences,
 * et naviguer vers le planning.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 * @version 1.0
 */
public class ProfilSecouristeAdminController implements Initializable {

    @FXML private Label timeLabel;
    @FXML private CheckBox customCheckbox, customCheckbox1, customCheckbox2, customCheckbox3,
            customCheckbox4, customCheckbox5, customCheckbox6, customCheckbox7, customCheckbox8;
    @FXML private ImageView checkboxImage, checkboxImage1, checkboxImage2, checkboxImage3,
            checkboxImage4, checkboxImage5, checkboxImage6, checkboxImage7, checkboxImage8;
    private Image checkedImage;
    private Image uncheckedImage;

    @FXML private VBox popupPane;
    @FXML private Rectangle overlay;
    @FXML private ScrollPane scrollPane;
    @FXML public Button backButton;

    @FXML private Label nomField;
    @FXML private Label prenomField;
    @FXML private Label adresseField;
    @FXML private Label telephoneField;

    private Secouriste secouriste;

    @FXML private HBox competenceContainer;
    @FXML private Label titrePopupLabel;

    @FXML private Button deleteAccountButton;


    /**
     * Initialise les champs, les compétences, et les composants visuels.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HeureController.afficherHeure(timeLabel);
        secouriste = GlobalController.getSelectedSecouriste();

        PossedeDAO possedeDAO = new PossedeDAO();
        List<Competences> competences = possedeDAO.findCompetencesBySecouriste(secouriste.getId());
        secouriste.setCompetences(competences);
        afficherPastillesCompetences(competences);

        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        // Chargement des images des cases cochées et décochées
        checkedImage = new Image(getClass().getResource("/ressources/img/case_coche.png").toExternalForm());
        uncheckedImage = new Image(getClass().getResource("/ressources/img/case_non_coche.png").toExternalForm());

        // Lier chaque checkbox à son image
        setupCheckbox(customCheckbox, checkboxImage);
        setupCheckbox(customCheckbox1, checkboxImage1);
        setupCheckbox(customCheckbox2, checkboxImage2);
        setupCheckbox(customCheckbox3, checkboxImage3);
        setupCheckbox(customCheckbox4, checkboxImage4);
        setupCheckbox(customCheckbox5, checkboxImage5);
        setupCheckbox(customCheckbox6, checkboxImage6);
        setupCheckbox(customCheckbox7, checkboxImage7);
        setupCheckbox(customCheckbox8, checkboxImage8);

        // Affichage des infos utilisateur
        nomField.setText(secouriste.getNom());
        prenomField.setText(secouriste.getPrenom());
        adresseField.setText(secouriste.getEmail());
        telephoneField.setText(secouriste.getTelephone() != null ? secouriste.getTelephone() : "");
    }

    /**
     * Lien dynamique entre une CheckBox et son image.
     */
    private void setupCheckbox(CheckBox checkBox, ImageView imageView) {
        imageView.setImage(uncheckedImage);
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            imageView.setImage(newVal ? checkedImage : uncheckedImage);
        });
    }

    /**
     * Initialise les cases cochées selon les compétences déjà acquises.
     */
    private void initialiserCheckboxesDepuisCompetences(List<Competences> competences) {
        List<String> competencesPossedees = new ArrayList<>();
        for (Competences c : competences) {
            competencesPossedees.add(c.getIntitule());
        }

        if (competencesPossedees.contains("CO")) { customCheckbox.setSelected(true); }
        if (competencesPossedees.contains("CP")) { customCheckbox1.setSelected(true); }
        if (competencesPossedees.contains("CE")) { customCheckbox2.setSelected(true); }
        if (competencesPossedees.contains("PBC")) { customCheckbox3.setSelected(true); }
        if (competencesPossedees.contains("PBF")) { customCheckbox4.setSelected(true); }
        if (competencesPossedees.contains("PSE1")) { customCheckbox5.setSelected(true); }
        if (competencesPossedees.contains("PSE2")) { customCheckbox6.setSelected(true); }
        if (competencesPossedees.contains("SSA")) { customCheckbox7.setSelected(true); }
        if (competencesPossedees.contains("VPSP")) { customCheckbox8.setSelected(true); }
    }

    /**
     * Revient à la vue précédente.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        if (!GlobalController.getViewHistory().isEmpty()) {
            Parent previousView = GlobalController.getViewHistory().pop();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(previousView);
            TranslateTransition transition = new TranslateTransition(Duration.millis(300), previousView);
            transition.setFromX(-scene.getWidth());
            transition.setToX(0);
            transition.play();
        }
    }

    /**
     * Redirige vers le calendrier du secouriste (vue semaine).
     */
    @FXML
    private void handleConsulterCalendrier(ActionEvent actionEvent) {
        try {
            GlobalController.switchView("../ressources/fxml/CalendrierSecouristeSemaineAdmin.fxml", (Node) actionEvent.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche le popup de sélection des compétences.
     */
    @FXML
    private void showPopup() {
        titrePopupLabel.setText("Sélectionner les compétences pour " + secouriste.getPrenom());
        popupPane.setVisible(true);
        overlay.setVisible(true);
        initialiserCheckboxesDepuisCompetences(secouriste.getCompetences());
    }

    /**
     * Ferme le popup de sélection des compétences.
     */
    @FXML
    private void hidePopup() {
        popupPane.setVisible(false);
        overlay.setVisible(false);
    }

    @FXML private void onBackHover() { backButton.setOpacity(0.7); }
    @FXML private void onBackExit() { backButton.setOpacity(1.0); }
    @FXML private void onBackPress() { backButton.setTranslateY(2); backButton.setOpacity(0.5); }
    @FXML private void onBackRelease() { backButton.setTranslateY(0); backButton.setOpacity(0.7); }

    /**
     * Récupère les compétences sélectionnées dans les checkboxes.
     */
    private List<String> getCompetencesSelectionnees() {
        List<String> competences = new ArrayList<>();
        if (customCheckbox.isSelected()) competences.add("CO");
        if (customCheckbox1.isSelected()) competences.add("CP");
        if (customCheckbox2.isSelected()) competences.add("CE");
        if (customCheckbox3.isSelected()) competences.add("PBC");
        if (customCheckbox4.isSelected()) competences.add("PBF");
        if (customCheckbox5.isSelected()) competences.add("PSE1");
        if (customCheckbox6.isSelected()) competences.add("PSE2");
        if (customCheckbox7.isSelected()) competences.add("SSA");
        if (customCheckbox8.isSelected()) competences.add("VPSP");
        return competences;
    }

    /**
     * Enregistre les compétences sélectionnées et les affiche.
     */
    @FXML
    private void handleValiderCompetences() {
        List<String> nouvellesCompetences = getCompetencesSelectionnees();

        PossedeDAO possedeDAO = new PossedeDAO();
        possedeDAO.deleteAllBySecouriste(secouriste.getId());

        for (String intitule : nouvellesCompetences) {
            possedeDAO.create(new Possede(intitule, secouriste.getId()));
        }

        List<Competences> competences = new ArrayList<>();
        for (String c : nouvellesCompetences) {
            competences.add(new Competences(c));
        }
        secouriste.setCompetences(competences);

        afficherPastillesCompetences(competences);
        hidePopup();
    }

    /**
     * Affiche les compétences sous forme de pastilles dans le profil.
     */
    private void afficherPastillesCompetences(List<Competences> competences) {
        competenceContainer.getChildren().clear();

        for (Competences comp : competences) {
            Label pastille = new Label(comp.getIntitule());
            pastille.setStyle("-fx-background-color: white; -fx-text-fill: #E60023; -fx-font-size: 20;" +
                    "-fx-border-color: #E60023; -fx-border-radius: 30; -fx-background-radius: 30;");
            pastille.setPadding(new Insets(10, 20, 10, 20));
            competenceContainer.getChildren().add(pastille);
        }
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le compte de " + secouriste.getPrenom() + " " + secouriste.getNom() + " ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PossedeDAO possedeDAO = new PossedeDAO();
            possedeDAO.deleteAllBySecouriste(secouriste.getId()); // Supprime les compétences liées

            SecouristeDAO secouristeDAO = new SecouristeDAO();
            int deleted = secouristeDAO.delete(secouriste); // Supprime le compte

            if (deleted > 0) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Compte supprimé");
                info.setHeaderText(null);
                info.setContentText("Le compte a été supprimé avec succès.");
                info.showAndWait();

                // Retour à la vue précédente
                handleBack(event);
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Erreur");
                error.setHeaderText("Échec de la suppression du compte.");
                error.setContentText("Une erreur est survenue lors de la suppression.");
                error.showAndWait();
            }
        }
    }
}
