package gruppo1.progettorubrica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
        Parent root = fxmlLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
