package rubricaNoGit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ImagePopupController implements Initializable {

    @FXML
    private ImageView imgAdd;

    @FXML
    private HBox defaultImgHBox;

    private File selectedImage;
    //Serve per capire quale delle 4 immagini di default è stata scelta (1 <= x <= 4)
    //oppure se è stata scelta immagine custom (id=5)
    private int imageIndex;

    public File getSelectedImage() {
        return this.selectedImage;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
