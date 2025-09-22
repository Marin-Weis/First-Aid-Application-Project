package vue;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class SecouristeView extends Application{

    @Override
    public void start(Stage primaryStage ) throws Exception {

        // Charger la police custom
        Font font = Font.loadFont(getClass().getResourceAsStream("../ressources/fonts/SF-Pro-Display-Medium.otf"), 12);
        System.out.println("Font loaded: " + (font != null));

        // Charger la page de chargement
        Parent loadingRoot = FXMLLoader.load(getClass().getResource("/ressources/fxml/LoadingScreen.fxml"));
        Scene loadingScene = new Scene(loadingRoot);
        loadingScene.setFill(Color.TRANSPARENT);

        // Configurer la fenêtre
        primaryStage.initStyle(StageStyle.TRANSPARENT); // Supprime la décoration native
        primaryStage.setScene(loadingScene);
        primaryStage.show();

        // Après 3 secondes, on charge la vraie page
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            try {
                Parent mainRoot = FXMLLoader.load(getClass().getResource("/ressources/fxml/Accueil.fxml"));
                Scene mainScene = new Scene(mainRoot);
                mainScene.setFill(Color.TRANSPARENT);
                primaryStage.setScene(mainScene);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        pause.play(); // Démarre le compte à rebour avant d'attendre 3 secondes pour lancer la pagge d'accueil
    }

    public static void main(String[] args) {
        launch(args);
    }

}
