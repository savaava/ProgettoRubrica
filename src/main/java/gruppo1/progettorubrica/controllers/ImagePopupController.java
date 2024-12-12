package gruppo1.progettorubrica.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;

/**
 * @brief Controller che visualizza il popup contenente immagini contatto.
 * 
 * In tale popup, l'utente può associare un'immagine ad un contatto, selezionandola dalle immagini suggerite o caricarne una personalizzata.
 */

public class ImagePopupController implements Initializable {

    //@FXML
    //private ImageView imgAdd; ///< Pulsante per poter caricare un'immagine personalizzata.

    @FXML
    private HBox defaultImgHBox; ///< Contiene le immagini suggerite.

    private File selectedImage; ///< File dell'immagine caricata dall'utente.
    
    private int imageIndex; ///< Indice dell'immagine scelta dall'utente.


    /**
     * @brief Carica l'image popup controller.
     * @pre Cliccare sull'immagine profilo di un contatto.
     * @post Visualizzare il popup per scegliere l'immagine da associare.
     * 
     * Questo metodo permette di visualizzare il popup image quando l'utente clicca sull'immagine di un contatto.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    

    /**
     * @pre L'utente ha caricato un file contenente l'immagine personalizzata.
     * @post Si ottiene il file caricato.
     * @return Il file dell'immagine caricata dall'utente.
     * 
     */
    public File getSelectedImage() {
        return this.selectedImage;
    }

    /**
     * @brief Indica se l'utente ha scelto un'immagine tra quelle suggerite o se ne ha caricata una personalizzata.
     * @pre 
     * @post Ritorna l'indice associato all'immagine profilo di un contatto.
     * @return L'indice (compreso tra 1 e 4) associato all'immagine selezionata, l'indice=5 se l'utente ha caricato un'immagine personalizzata altrimenti 0 se al contatto è associata l'immagine di default.
     */
    public int getImageIndex() {
        return this.imageIndex;
    }

    @FXML
    private void onImageClicked1(MouseEvent event) {
        this.imageIndex=1;
    }

    @FXML
    private void onImageClicked2(MouseEvent event) {
        this.imageIndex=2;
    }

    @FXML
    private void onImageClicked3(MouseEvent event) {
        this.imageIndex=3;
    }

    @FXML
    private void onImageClicked4(MouseEvent event) {
        this.imageIndex=4;
    }

    @FXML
    private void onImageClicked5(MouseEvent event) {
        FileChooser fc=new FileChooser();
        fc.setTitle("Seleziona un'immagine");
        fc.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedImage = fc.showOpenDialog(new Popup());
        if(selectedImage!=null){
            this.selectedImage=selectedImage;
            this.imageIndex=5;
        }
        else
            this.imageIndex=0;
    }

}

