package gruppo1.progettorubrica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @brief Questa classe visualizzerà la scena principale all'avvio dell'applicazione.
 */
public class AddressBookMain extends Application {

    /**
     * @brief Metodo principale.
     *
     *  Questa è la funzione principale del programma. Viene eseguita all'avvio del programma 
     *  e lancia l'applicazione JavaFX.
     *
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @brief Crea la struttura di base dell'applicazione JavaFX.
     *
     *  Questo metodo carica la scena principale Main.fxml.
     *
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root, 1000, 600));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_rubrica.png")));
            primaryStage.setTitle("Rubrica Gruppo 1");
            primaryStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di caricamento");
            alert.setHeaderText("Impossibile caricare il file FXML");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
