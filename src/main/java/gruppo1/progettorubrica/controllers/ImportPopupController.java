package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import gruppo1.progettorubrica.models.ContactManager;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.services.Converter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * @brief Controller che visualizza il popup per l'import di una rubrica.
 * 
 * Tramite tale popup è possibile selezionare il percorso del file .csv o .vCard da cui importare la rubrica.
 */
public class ImportPopupController implements Initializable {
    private ContactManager contactManager;  ///< Istanza dell'interfaccia con i metodi utilizzabili dal controller.
    
    private File file; 
    
    @FXML
    private Button importButton;   ///< Riferimento al pulsante per importare la rubrica da file.

    /**
     * @brief Inizializzazione controller.
     *
     *  Tramite questo metodo carichiamo su questo controller l'istanza della rubrica in esame 
     *  utilizzando il metodo getInstance {@link AddressBook#getInstance()}.
     * 
     * @param[in] location
     * @param[in] resources 
     * 
     * @pre Nessuna
     * @post Il controller ha un'istanza di AddresssBook
     */
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contactManager = AddressBook.getInstance();
    }
    
    /**
     * @brief  Selezione percorso.
     *
     *  Tramite questo metodo l'utente fornisce al controller il file di import della rubrica.
     * 
     * @param[in] event
     */
    @FXML
    private void selectFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleziona file di import");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("CSV", "*.csv"),
                new ExtensionFilter("VCard", "*.vcf")
        );
        this.file = fc.showOpenDialog(new Stage());
    }

    /**
     * @brief Importa rubrica.
     *
     * Questo metodo, che viene invocato cliccando il pulsante "Importa", permette di importare la rubrica.
     * @param[in] event
     */
    
    @FXML
    private void onImport(ActionEvent event) {
        try {
            if (Converter.checkCSVFormat(this.file)) {
                Converter.parseCSV(this.file);
            } else if (Converter.checkVCardFormat(this.file)) {
                Converter.parseVCard(this.file);
            }
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore IO");
            alert.setContentText("Dettagli: " + ex.getMessage());
        }
    }

}
