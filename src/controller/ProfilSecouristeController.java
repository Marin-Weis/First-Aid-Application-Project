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
 * Contrôleur du profil secouriste. Permet l'affichage et la modification
 * des informations personnelles et des compétences, avec interface interactive.
 *
 * @author M. Weis, N. Lescop, M. Gouelo, A. Jan
 */
public class ProfilSecouristeController implements Initializable {

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

    @FXML private Label nomField, prenomField, adresseField, telephoneField;
    @FXML private HBox competenceContainer;
    @FXML private Label titrePopupLabel;

    private Secouriste user;

    /**
     * Initialisation du contrôleur : affichage des infos et des compétences.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HeureController.afficherHeure(timeLabel);
        this.user = GlobalController.getCurrentUser();

        // Recharger les compétences du secouriste
        PossedeDAO possedeDAO = new PossedeDAO();
        List<Competences> competences = possedeDAO.findCompetencesBySecouriste(user.getId());
        user.setCompetences(competences);
        afficherPastillesCompetences(competences);

        // Scroll plus rapide
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY() * 3;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight());
            event.consume();
        });

        // Chargement des images pour les checkboxes
        checkedImage = new Image(getClass().getResource("/ressources/img/case_coche.png").toExternalForm());
        uncheckedImage = new Image(getClass().getResource("/ressources/img/case_non_coche.png").toExternalForm());

        // Association des checkboxes à leurs images
        setupCheckbox(customCheckbox, checkboxImage);
        setupCheckbox(customCheckbox1, checkboxImage1);
        setupCheckbox(customCheckbox2, checkboxImage2);
        setupCheckbox(customCheckbox3, checkboxImage3);
        setupCheckbox(customCheckbox4, checkboxImage4);
        setupCheckbox(customCheckbox5, checkboxImage5);
        setupCheckbox(customCheckbox6, checkboxImage6);
        setupCheckbox(customCheckbox7, checkboxImage7);
        setupCheckbox(customCheckbox8, checkboxImage8);

        // Remplissage des champs texte
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        adresseField.setText(user.getEmail());
        telephoneField.setText(user.getTelephone() != null ? user.getTelephone() : "");
    }

    /**
     * Associe une checkbox à son image de coche.
     */
    private void setupCheckbox(CheckBox checkBox, ImageView imageView) {
        imageView.setImage(uncheckedImage);
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            imageView.setImage(newVal ? checkedImage : uncheckedImage);
        });
    }

    /**
     * Coche automatiquement les cases si l'utilisateur possède déjà ces compétences.
     */
    private void initialiserCheckboxesDepuisCompetences(List<Competences> competences) {
        List<String> existantes = new ArrayList<>();
        for (Competences c : competences) existantes.add(c.getIntitule());

        if (existantes.contains("CO")) customCheckbox.setSelected(true);
        if (existantes.contains("CP")) customCheckbox1.setSelected(true);
        if (existantes.contains("CE")) customCheckbox2.setSelected(true);
        if (existantes.contains("PBC")) customCheckbox3.setSelected(true);
        if (existantes.contains("PBF")) customCheckbox4.setSelected(true);
        if (existantes.contains("PSE1")) customCheckbox5.setSelected(true);
        if (existantes.contains("PSE2")) customCheckbox6.setSelected(true);
        if (existantes.contains("SSA")) customCheckbox7.setSelected(true);
        if (existantes.contains("VPSP")) customCheckbox8.setSelected(true);
    }

    /**
     * Retour vers le tableau de bord.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/TableauDeBord.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Déconnecte l'utilisateur et redirige vers l’accueil.
     */
    @FXML
    private void handleDeconnexion(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/Accueil.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void onBackHover() { backButton.setOpacity(0.7); }
    @FXML private void onBackExit() { backButton.setOpacity(1.0); }
    @FXML private void onBackPress() { backButton.setTranslateY(2); backButton.setOpacity(0.5); }
    @FXML private void onBackRelease() { backButton.setTranslateY(0); backButton.setOpacity(0.7); }

    /**
     * Récupère la liste des compétences cochées par l'utilisateur.
     */
    private List<String> getCompetencesSelectionnees() {
        List<String> list = new ArrayList<>();
        if (customCheckbox.isSelected()) list.add("CO");
        if (customCheckbox1.isSelected()) list.add("CP");
        if (customCheckbox2.isSelected()) list.add("CE");
        if (customCheckbox3.isSelected()) list.add("PBC");
        if (customCheckbox4.isSelected()) list.add("PBF");
        if (customCheckbox5.isSelected()) list.add("PSE1");
        if (customCheckbox6.isSelected()) list.add("PSE2");
        if (customCheckbox7.isSelected()) list.add("SSA");
        if (customCheckbox8.isSelected()) list.add("VPSP");
        return list;
    }

    /**
     * Enregistre les compétences sélectionnées et met à jour l'affichage.
     */
    @FXML
    private void handleValiderCompetences() {
        List<String> nouvelles = getCompetencesSelectionnees();

        PossedeDAO possedeDAO = new PossedeDAO();
        possedeDAO.deleteAllBySecouriste(user.getId());

        for (String intitule : nouvelles) {
            possedeDAO.create(new Possede(intitule, user.getId()));
        }

        List<Competences> competences = new ArrayList<>();
        for (String c : nouvelles) competences.add(new Competences(c));
        user.setCompetences(competences);

        afficherPastillesCompetences(competences);
        hidePopup();
    }

    /**
     * Enregistre les modifications des informations personnelles.
     */
    @FXML
    private void handleSaveModifications(ActionEvent event) {
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setEmail(adresseField.getText());
        user.setTelephone(telephoneField.getText());

        new SecouristeDAO().update(user);
        System.out.println("Profil mis à jour !");
    }

    /**
     * Redirige vers l’écran de modification d’informations personnelles.
     */
    @FXML
    private void handleEditInfos(ActionEvent event) {
        try {
            GlobalController.switchView("../ressources/fxml/InfosSecouriste.fxml", (Node) event.getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche les compétences du secouriste sous forme de pastilles.
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

    /**
     * Affiche la fenêtre popup pour gérer les compétences.
     */
    @FXML
    private void showPopup() {
        titrePopupLabel.setText("Sélectionner les compétences pour " + user.getPrenom());
        popupPane.setVisible(true);
        overlay.setVisible(true);
        initialiserCheckboxesDepuisCompetences(user.getCompetences());
    }

    /**
     * Ferme la fenêtre popup.
     */
    @FXML
    private void hidePopup() {
        popupPane.setVisible(false);
        overlay.setVisible(false);
    }

    @FXML
    private void handleDeleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer votre compte ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            PossedeDAO possedeDAO = new PossedeDAO();
            possedeDAO.deleteAllBySecouriste(user.getId()); // ← SUPPRESSION DES LIENS AVANT

            SecouristeDAO secouristeDAO = new SecouristeDAO();
            int deleted = secouristeDAO.delete(user);

            if (deleted > 0) {
                GlobalController.currentUser = null;
                try {
                    GlobalController.switchView("../ressources/fxml/Accueil.fxml", (Node) event.getSource());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Échec de la suppression du compte.");
                errorAlert.showAndWait();
            }
        }
    }
}
