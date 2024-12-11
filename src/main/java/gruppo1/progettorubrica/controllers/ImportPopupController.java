package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import gruppo1.progettorubrica.models.ContactManager;
import gruppo1.progettorubrica.models.AddressBook;
import gruppo1.progettorubrica.models.Contact;
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
                new ExtensionFilter("CSV", ".csv"),
                new ExtensionFilter("VCard", ".vcf")
        );
        this.file = fc.showOpenDialog(new Stage());
        if(this.file == null) return;
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
            if (checkCSVFormat()) {
                parseCSV();
            } else if (checkVCardFormat()) {
                parseVCard();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void parseCSV() throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("[,\n]");
            s.useLocale(Locale.ITALY);
            if (s.nextLine() == null) return;
            while (s.hasNext()) {
                String nome = s.next();
                String cognome = s.next();
                String num1 = s.next();
                String num2 = s.next();
                String num3 = s.next();
                String em1 = s.next();
                String em2 = s.next();
                String em3 = s.next();
                String pp = s.next();
                byte[] fileContent = Base64.getDecoder().decode(pp);
                Contact c = new Contact(nome, cognome);
                c.setNumbers(new String[]{num1, num2, num3});
                c.setEmails(new String[]{em1, em2, em3});
                c.setProfilePicture(fileContent);
                contactManager.addContact(c);
            }
        }
    }

    private void parseVCard() throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("[;\n]");
            s.useLocale(Locale.ITALY);
            List<String> n = new ArrayList<>();
            List<String> em = new ArrayList<>();
            String nome = null, cognome = null;
            byte[] fileContent = null;
            if (s.nextLine() == null) return;
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("N:")) {
                    String[] nameParts = line.substring(2).split(";");
                    cognome = nameParts[0];
                    nome = nameParts[1];
                } else if (line.startsWith("TEL;")) {
                    if (n.size() < 3) n.add(line.substring(line.indexOf(":") + 1));
                } else if (line.startsWith("EMAIL;")) {
                    if (em.size() < 3) em.add(line.substring(line.indexOf(":") + 1));
                } else if (line.startsWith("PHOTO;")) {
                    fileContent = Base64.getDecoder().decode(line.substring(line.indexOf(":") + 1));
                } else if (line.startsWith("END:VCARD")) {
                    Contact c = new Contact(nome, cognome);
                    c.setNumbers(n.toArray(new String[0]));
                    c.setEmails(em.toArray(new String[0]));
                    c.setProfilePicture(fileContent);
                    contactManager.addContact(c);
                    nome = null;
                    cognome = null;
                    fileContent = null;
                    n.clear();
                    em.clear();
                }
            }
        }
    }

    /**
     * @brief Controllo formato .csv.
     *
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     */
    
    private boolean checkCSVFormat() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.contains(",");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @brief Controllo formato .VCard.
     *
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     * 
     */
    private boolean checkVCardFormat() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.contains("BEGIN:VCARD");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
