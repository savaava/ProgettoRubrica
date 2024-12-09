package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
import gruppo1.progettorubrica.models.ContactManager;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @brief Controller che visualizza il popup per l'export della rubrica.
 * 
 * Tramite tale popup è possibile selezionare il percorso del file .csv o .vCard in cui esportare la rubrica.
 */

public class ExportPopupController implements Initializable {
    private ContactManager contactManager; ///< Riferimento all'interfaccia ContactManager, implementata da AddressBook.
    
    private File file;  ///< Riferimento al percorso di export del file

    @FXML
    private ChoiceBox<String> exportChoiceBox; ///< Permette all'utente di scegliere quali contatti esportare.

    @FXML
    private Button saveButton; ///<Pulsante per concludere l'esportazione.
    

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
     * @brief Scelta del percorso del file di esportazione.
     *
     * Invocando questo metodo il controller permette all'utente di scegliere 
     * il path su dove salvare il file di export della rubrica.
     *
     * @param[in] event
     */
    @FXML
    private void choosePath(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Scegliere percorso file");
        this.file = filechooser.showSaveDialog(new Stage());
        if(this.file == null) return;
    }

    /**
     * @brief Esporta la rubrica.
     * 
     * Questo metodo, che viene invocato cliccando il pulsante "Esporta", permette di esportare la rubrica.
     * @param[in] event 
     * @see onExportCSV(ActionEvent event)
     * @see onExportVCard(ActionEvent event)
     */
    @FXML
    private void onExport(ActionEvent event) {
        String f = this.file.toString();
        String ext = f.substring(f.indexOf(".") + 1);
        if(ext.equalsIgnoreCase("csv")) this.onExportCSV(null);
        else if(ext.equalsIgnoreCase("vcf")) this.onExportVCard(null);
    }
    
    
    /**
     * @brief Esporta la rubrica in un file nel formato .csv.
     * @param[in] event 
     */
    private void onExportCSV(ActionEvent event) {
        ObservableList<Contact> ol = contactManager.getAllContacts();
        Iterator<Contact> i = ol.iterator();
        try(PrintWriter p =  new PrintWriter(new BufferedWriter(new FileWriter(this.file)))){
            p.println("Nome,Cognome,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO");
            while(i.hasNext()){
                Contact c = i.next();
                StringBuffer sb = new StringBuffer();
                sb.append(c.getName()).append(",").append(c.getSurname()).append(",");
                for (String t : c.getNumbers()) sb.append(t != null ? t : "").append(",");
                for (String e : c.getEmails()) sb.append(e != null ? e : "").append(",");
                sb.append(c.getProfilePicture() != null ? Base64.getEncoder().encodeToString(c.getProfilePicture()) : "").append("\n");
                p.println(sb.toString());
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @brief Esporta la rubrica in un file nel formato .vCard.
     * @param[in] event 
     */
    private void onExportVCard(ActionEvent event) {
        ObservableList<Contact> ol = contactManager.getAllContacts();
        Iterator<Contact> i = ol.iterator();
        try(PrintWriter p =  new PrintWriter(new BufferedWriter(new FileWriter(this.file)))){
            while(i.hasNext()){
                Contact c = i.next();
                p.println("BEGIN:VCARD\n");
                p.println("VERSION:3.0\n");
                p.println("FN:" + c.getName() + " " + c.getSurname() + "\n");
                p.println("N: " + c.getSurname() + ";" +c.getName() + ";\n"); 
                for(String t : c.getNumbers()) p.println("TEL; TYPE:" + t + "\n");
                for(String e : c.getEmails()) p.println("EMAIL; TYPE: " + e + "\n");
                p.println(Base64.getEncoder().encodeToString(c.getProfilePicture())+ "\n");
                p.println("END:VCARD");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
