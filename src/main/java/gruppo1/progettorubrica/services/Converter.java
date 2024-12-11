/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppo1.progettorubrica.services;

import gruppo1.progettorubrica.models.Contact;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

/**
 * @brief Classe di utility per l'import ed export della rubrica
 */
public class Converter {
    
    public static Collection<Contact> parseCSV(File file) throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("[,\n]");
            s.useLocale(Locale.ITALY);
            List<Contact> contatti = new ArrayList<>();
            if (s.hasNext() == false) return Collections.emptyList();
            s.nextLine();
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
                contatti.add(c);
            }
            return contatti;
        }
    }

    public static Collection<Contact> parseVCard(File file) throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("[;\n]");
            s.useLocale(Locale.ITALY);
            List<String> n = new ArrayList<>();
            List<String> em = new ArrayList<>();
            List<Contact> contatti = new ArrayList<>();
            String nome = null, cognome = null;
            byte[] fileContent = null;
            if (s.nextLine() == null) return Collections.emptyList();
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
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     */
    
    public static boolean checkCSVFormat(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.contains(",");
        }
    }

    /**
     * @brief Controllo formato .VCard.
     *
     *  Tramite questo metodo il controller verifica che il file fornito sia di formato .csv.
     * 
     */
    public static boolean checkVCardFormat(File file)throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.contains("BEGIN:VCARD");
        }
    }
    
    /**
     * @brief Esporta la rubrica in un file nel formato .csv.
     * @param[in] event 
     */
    public static void onExportCSV(Collection<Contact> contacts, File file) throws IOException{
        Iterator<Contact> i = contacts.iterator();
        try(PrintWriter p =  new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            p.println("Name,Surname,TEL1,TEL2,TEL3,EMAIL1,EMAIL2,EMAIL3,PHOTO,");
            while(i.hasNext()){
                Contact c = i.next();
                StringBuffer sb = new StringBuffer();
                sb.append(c.getName()).append(",").append(c.getSurname()).append(",");
                for (String t : c.getNumbers()) sb.append(t != null ? t : "").append(",");
                for (String e : c.getEmails()) sb.append(e != null ? e : "").append(",");
                sb.append(c.getProfilePicture() != null ? Base64.getEncoder().encodeToString(c.getProfilePicture()) : "").append(",\n");
                p.println(sb.toString());
            }
            
        }
    }
    
    /**
     * @brief Esporta la rubrica in un file nel formato .vCard.
     * @param[in] event 
     */
    public static void onExportVCard(Collection<Contact> contacts, File file) throws IOException{
        Iterator<Contact> i = contacts.iterator();
        try(PrintWriter p =  new PrintWriter(new BufferedWriter(new FileWriter(file)))){
            while(i.hasNext()){
                Contact c = i.next();
                p.println("BEGIN:VCARD\n");
                p.println("VERSION:3.0\n");
                p.println("FN:" + c.getName() + " " + c.getSurname() + "\n");
                p.println("N: " + c.getSurname() + ";" +c.getName() + ";\n"); 
                for(String t : c.getNumbers()) p.println("TEL; TYPE:" + t + "\n");
                for(String e : c.getEmails()) p.println("EMAIL; TYPE:" + e + "\n");
                p.println(Base64.getEncoder().encodeToString(c.getProfilePicture())+ "\n");
                p.println("END:VCARD");
            }
            
        }
    }
}
