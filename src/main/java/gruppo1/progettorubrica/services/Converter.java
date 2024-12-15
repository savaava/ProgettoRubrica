package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

/**
 * @brief Classe di utility per l'import ed export della rubrica
 */
public class Converter {

    /**
     * @brief Creazione collezione con i contatti da .csv
     *
     * @pre Il file CSV deve essere formattato correttamente e non essere vuoto.
     * @post I contatti estratti vengono restituiti in una collezione.
     *
     * Questo metodo permette di estrarre da un file .csv i contatti e li salva in una collezione
     * @param[in] file
     * @return Una collezione di contatti
     * @throws IOException
     */
    public static Collection<Contact> parseCSV(File file) throws IOException {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",",-1);

                String name = values[0];
                String surname = values[2];
                String[] numbers = new String[3];
                String[] emails = new String[3];
            
                numbers[0] = values[24];
                numbers[1] = values[26];
                numbers[2] = values[28];
            
                emails[0] = values[18];
                emails[1] = values[20];
                emails[2] = values[22];

                Byte[] profilePicture = Converter.stringToByteArray(values[15]);

                Contact c = new Contact(name, surname);
                c.setNumbers(numbers);
                c.setEmails(emails);
                c.setProfilePicture(profilePicture);

                contacts.add(c);
            }
        }
        return contacts;
    }

    /**
     * @brief Creazione collezione con i contatti da .vcf
     *
     * @pre Il file VCard deve essere formattato correttamente e non essere vuoto.
     * @post I contatti estratti vengono restituiti in una collezione.
     *
     * Questo metodo permette di estrarre da un file .vcf i contatti e li salva in una collezione
     * @param[in] file
     * @return Una collezione di contatti
     * @throws IOException
     */
    public static Collection<Contact> parseVCard(File file) throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("[;\n]");
            s.useLocale(Locale.ITALY);
            List<String> n = new ArrayList<>();
            List<String> em = new ArrayList<>();
            List<Contact> contatti = new ArrayList<>();
            String nome = null, cognome = null;
            Byte[] fileContent = null;
            if (s.hasNext() == false) return Collections.emptyList();
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
                } else if (line.startsWith("PHOTO:")) {
                    fileContent = stringToByteArray(line.substring(line.indexOf(":") + 1));
                } else if (line.startsWith("END:VCARD")) {
                    Contact c = new Contact(nome, cognome);
                    c.setNumbers(n.toArray(new String[0]));
                    c.setEmails(em.toArray(new String[0]));
                    c.setProfilePicture(fileContent);
                    contatti.add(c);
                    nome = null;
                    cognome = null;
                    fileContent = null;
                    n.clear();
                    em.clear();
                }
            }
            return contatti;
        }
    }

    /**
     * @brief Controllo formato .csv.
     *
     * @pre Il file deve essere un file di testo leggibile.
     * @post Viene restituito un valore booleano che indica se il file è in formato .csv.
     *
     * Questo metodo verifica che il file fornito sia in formato .csv.
     * @param[in] file Il file da controllare
     * @return Il risultato del controllo
     * @throws IOException
     */

    public static boolean checkCSVFormat(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            System.out.println(firstLine);
            System.out.println("First Name,Middle Name,Last Name,Phonetic First Name,Phonetic Middle Name,Phonetic Last Name,Name Prefix,Name Suffix,Nickname,File As,Organization Name,Organization Title,Organization Department,Birthday,Notes,Photo,Labels,E-mail 1 - Label,E-mail 1 - Value,E-mail 2 - Label,E-mail 2 - Value,E-mail 3 - Label,E-mail 3 - Value,Phone 1 - Label,Phone 1 - Value,Phone 2 - Label,Phone 2 - Value,Phone 3 - Label,Phone 3 - Value,Custom Field 1 - Label,Custom Field 1 - Value\\n\"");
            return firstLine != null && firstLine.equalsIgnoreCase("First Name,Middle Name,Last Name,Phonetic First Name,Phonetic Middle Name,Phonetic Last Name,Name Prefix,Name Suffix,Nickname,File As,Organization Name,Organization Title,Organization Department,Birthday,Notes,Photo,Labels,E-mail 1 - Label,E-mail 1 - Value,E-mail 2 - Label,E-mail 2 - Value,E-mail 3 - Label,E-mail 3 - Value,Phone 1 - Label,Phone 1 - Value,Phone 2 - Label,Phone 2 - Value,Phone 3 - Label,Phone 3 - Value,Custom Field 1 - Label,Custom Field 1 - Value");
        }
    }

    /**
     * @brief Controllo formato VCard.
     *
     * @pre Il file deve essere un file di testo leggibile.
     * @post Viene restituito un valore booleano che indica se il file è in formato VCard.
     *
     *  Questo metodo verifica che il file fornito sia in formato VCard.
     * @param[in] file Il file da controllare
     * @return Il risultato del controllo
     * @throws IOException
     */
    public static boolean checkVCardFormat(File file)throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.contains("BEGIN:VCARD");
        }
    }

    /**
     * @brief Esporta la rubrica in un file nel formato .csv.
     *
     * @pre Il chiamante deve fornire una collezione di contatti valida e un percorso file valido.
     * @post I contatti sono scritti in formato CSV nel file specificato.
     *
     * Questo metodo scrive i contatti in un file .csv
     * @param[in] contacts La collezione di contatti da esportare.
     * @param[in] file Il percorso del file dove salvare il CSV.
     * @throws IOException
     */
    public static void onExportCSV(Collection<Contact> contacts, File file) throws IOException{
        Iterator<Contact> i = contacts.iterator();
        try(PrintWriter p =  new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            p.println("First Name,Middle Name,Last Name,Phonetic First Name,Phonetic Middle Name,Phonetic Last Name,Name Prefix,Name Suffix,Nickname,File As,Organization Name,Organization Title,Organization Department,Birthday,Notes,Photo,Labels,E-mail 1 - Label,E-mail 1 - Value,E-mail 2 - Label,E-mail 2 - Value,E-mail 3 - Label,E-mail 3 - Value,Phone 1 - Label,Phone 1 - Value,Phone 2 - Label,Phone 2 - Value,Phone 3 - Label,Phone 3 - Value,Custom Field 1 - Label,Custom Field 1 - Value");
            while(i.hasNext()){
                Contact c = i.next();
                StringBuffer sb = new StringBuffer();
                sb.append(c.getName()).append(",").append(",").append(c.getSurname()).append(","); //fino Last Name
                for(int k = 0; k<12; k++) sb.append(",");  //tutti campi non implementati nel programma
                sb.append(c.getProfilePicture() != null ? byteArrayToString(c.getProfilePicture()) : "").append(",").append(",");  //photo+label
                for (String e : c.getEmails()) {
                    sb.append(",");   //label email
                    sb.append(e != null ? e : "").append(",");
                }
                for (String t : c.getNumbers()){
                    sb.append(",");   //label telefono
                    sb.append(t != null ? t : "").append(",");
                }
                sb.append(",,");   //customField non valorizzato
                p.println(sb.toString());
            }

        }
    }

    /**
     * @brief Esporta la rubrica in un file nel formato .vcf.
     *
     * @pre Il chiamante deve fornire una collezione di contatti valida e un percorso file valido.
     * @post I contatti sono scritti in formato VCard nel file specificato.
     *
     * Questo metodo scrive i contatti in un file .vcf.
     * @param[in] contacts La collezione di contatti da esportare.
     * @param[in] file Il percorso del file dove salvare il CSV.
     * @throws IOException
     */
    public static void onExportVCard(Collection<Contact> contacts, File file) throws IOException {
        try (PrintWriter p = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            for (Contact c : contacts) {
                p.println("BEGIN:VCARD");
                p.println("VERSION:3.0");
                p.println("FN:" + c.getName() + " " + c.getSurname());
                p.println("N:" + c.getSurname() + ";" + c.getName() + ";");
                for (String t : c.getNumbers()) {
                    if (t != null && !t.isEmpty()) {
                        p.println("TEL;TYPE:" + t);
                    }
                }
                for (String e : c.getEmails()) {
                    if (e != null && !e.isEmpty()) {
                        p.println("EMAIL;TYPE:" + e);
                    }
                }
                if (c.getProfilePicture() != null) {
                    String Photo = byteArrayToString(c.getProfilePicture());
                    p.println("PHOTO:" + Photo);
                }
                p.println("END:VCARD");
            }
        }
    }

    /**
     * @brief Converte una stringa in un array di byte.
     * @param[in] originalString La stringa da convertire.
     * @return L'array di byte corrispondente alla stringa.
     * 
     * @pre La stringa non deve essere vuota.
     * @post L'array di byte restituito è la decodifica Base64 della stringa.
     */
    public static Byte[] stringToByteArray(String originalString) {
        return Converter.toWrapper(Base64.getDecoder().decode(originalString));
    }

    /**
     * @brief Converte un array di byte in una stringa.
     * @param[in] byteObjectArray L'array di byte da convertire.
     * @return La stringa corrispondente all'array di byte.
     * 
     * @pre L'array di byte non deve essere vuoto.
     * @post La stringa restituita è la codifica Base64 dell'array di byte.
     * 
     */
    public static String byteArrayToString(Byte[] byteObjectArray) {
        return Base64.getEncoder().encodeToString(Converter.toPrimitive(byteObjectArray));
    }

    /**
     * @brief Converte un array di byte in un array di Byte.
     * @param[in] byteArray L'array di byte da convertire.
     * @return L'array di Byte corrispondente all'array di byte.
     * 
     * @pre L'array di byte non deve essere vuoto.
     * @post L'array di Byte restituito è una copia dell'array di byte.
     * 
     */
    public static Byte[] toWrapper(byte[] byteArray) {
        Byte[] byteObjects = new Byte[byteArray.length];
        for(int i=0; i<byteArray.length; i++) {
            byteObjects[i] = byteArray[i];
        }
        return byteObjects;
    }

    /**
     * @brief Converte un array di Byte in un array di byte.
     * @param[in] byteObjectArray L'array di Byte da convertire.
     * @return L'array di byte corrispondente all'array di Byte.
     * 
     * @pre L'array di Byte non deve essere vuoto.
     * @post L'array di byte restituito è una copia dell'array di Byte.
     * 
     */
    public static byte[] toPrimitive(Byte[] byteObjectArray) {
        byte[] byteArray = new byte[byteObjectArray.length];
        for(int i=0; i<byteObjectArray.length; i++) {
            byteArray[i] = byteObjectArray[i];
        }
        return byteArray;
    }

    /**
     * @brief Converte un'immagine in un array di Byte.
     * @param[in] imageView L'immagine da convertire.
     * @return L'array di Byte corrispondente all'immagine.
     * 
     * @pre L'ImageView non deve essere null
     * @post L'array di Byte restituito è la rappresentazione in byte dell'immagine.
     * 
     */
    public static Byte[] imageViewToByteArray(ImageView imageView) throws IOException {
        Image fxImage = imageView.getImage();
        if (fxImage == null) throw new IOException("L'ImageView non contiene nessuna immagine.");

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return toWrapper(byteArray);
    }
}