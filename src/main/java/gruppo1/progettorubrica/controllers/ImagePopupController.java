/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppo1.progettorubrica.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;

/**
 * @brief Controller che visualizza il popup contenente immagini contatto.
 * 
 * In tale popup, l'utente può associare un'immagine ad un contatto, selezionandola dalle immagini suggerite o caricarne una personalizzata.
 */

public class ImagePopupController implements Initializable {

    @FXML
    private ImageView imgAdd; ///< Pulsante per poter caricare un'immagine personalizzata.

    @FXML
    private HBox defaultImgHBox; ///< Contiene le immagini suggerite.

    private File selectedImage; ///< File dell'immagine caricata dall'utente.
    
    private int imageIndex; ///< Indice dell'immagine scelta dall'utente.


    /**
     * @brief Carica l'image popup controller.
     * 
     * Questo metodo permette di visualizzare il popup image quando l'utente clicca sull'immagine di un contatto.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    
    /**
    * @brief L'utente sceglie il file da cui caricare l'immagine personalizzata.
    */
    @FXML
    private void onImageAdd(MouseEvent event) {
        
    }

    /**
     * @return Il file dell'immagine caricata dall'utente.
     * 
     */
    public File getSelectedImage() {
        return this.selectedImage;
    }

    /**
     * @brief Indica se l'utente ha scelto un'immagine tra quelle suggerite o se ne ha caricata una personalizzata.
     * @return L'indice (compreso tra 1 e 4) associato all'immagine selezionata, altrimenti l'indice=5 se l'utente ha caricato un'immagine personalizzata.
     */
    public int getImageIndex() {
        return this.imageIndex;
    }

    
    
    
    
}

