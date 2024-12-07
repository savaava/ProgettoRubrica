package gruppo1.progettorubrica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * @brief Controller che visualizza il popup di conferma di un'operazione.
 * 
 * Tale popup consente all'utente di confermare o annullare un'operazione.
 */
public class ConfirmPopupController {
    private boolean choice; ///< Scelta dell'utente

    /**
     * @brief Restituisce la scelta dell'utente.
     * @return true se l'utente ha confermato l'operazione, false altrimenti
     */
    public boolean getChoice() {
        return choice;
    }

    /**
     * @brief Tale metodo conferma l'operazione.
     * @param[in] event 
     */
    @FXML
    private void onConfirm(ActionEvent event) {

    }

    /**
     * @brief Tale metodo annulla l'operazione.
     * @param[in] event 
     */
    @FXML
    private void onCancel(ActionEvent event) {

    }
}
