package com.example.pi.controllers;

import com.example.pi.models.SwitchScene;
import com.example.pi.models.cheque;
import com.example.pi.services.chequeservice;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumMap;
import java.util.Map;

public class Modifier {

    @FXML
    private TextField id;

    @FXML
    private TextField numero_compteTF;
    @FXML
    private TextField beneficiareTF1;
    @FXML
    private TextField date_emissionTF;
    @FXML
    private TextField emetteurTF1;
    @FXML
    private AnchorPane modificationcheque;
    @FXML
    private TextField montantTF1;
    @FXML
    private AnchorPane ajouterpage1;


public int x;
    private  cheque c ;
    private final chequeservice cs = new chequeservice();
    private String[] qrCodeParts;

    public void setC(cheque c) {
        this.c = c;
    }
    public void initData(cheque ch) {
        if (ch != null) {

            numero_compteTF.setText(ch.getNumero_compte());
            date_emissionTF.setText(ch.getDate_emission());  // Assurez-vous que le type est correct
            emetteurTF1.setText(ch.getEmetteur());
            beneficiareTF1.setText(ch.getBeneficiaire());
            montantTF1.setText(String.valueOf(ch.getMontant()));
        }
    }

    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean controlSaisie(TextField field, String fieldName) {
        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ vide", "Remplir le champ " + fieldName);
            return false;
        }
        return true;
    }

    public boolean controlSaisieDate(TextField field, String fieldName) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(field.getText(), formatter);
            return true;
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", fieldName + " doit être une date au format DD-MM-YYYY");
            return false;
        }
    }

    @FXML
    void ok(ActionEvent event) throws SQLException {
        System.out.println(x);
        float montant = Float.parseFloat(montantTF1.getText());
        if (controlSaisie(date_emissionTF, "date émission") && controlSaisie(emetteurTF1, "émetteur")
                && controlSaisie(montantTF1, "montant") && controlSaisie(beneficiareTF1, "bénéficiaire")) {
            if (controlSaisieDate(date_emissionTF, "date émission")) {

               if (x==0){cheque ch = new cheque(c.getNumero_de_cheque(),montant,numero_compteTF.getText(),beneficiareTF1.getText(),emetteurTF1.getText(),date_emissionTF.getText(), cheque.Statut.ENCAISSE);
                   cs.modifier(ch);
                   System.out.println(ch);
               }

else{
                   cheque ch = new cheque(Integer.parseInt( id.getText()),Float.parseFloat(montantTF1.getText()),numero_compteTF.getText(),beneficiareTF1.getText(),emetteurTF1.getText(),date_emissionTF.getText(),cheque.Statut.ENCAISSE);
                   cs.modifier(ch);
                   System.out.println(ch);


               }




                showAlert(Alert.AlertType.INFORMATION, "Succès", "Chèque modifié avec succès");
                Stage stage = (Stage) modificationcheque.getScene().getWindow();
                stage.close();
            }
        }

    }




    @FXML
    public void scannerQRcode(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image QR Code");
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                String result = decodeQRCode(bufferedImage);
                 updateFieldsWithQRCodeData(result);

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'image QR Code.");
            } catch (NotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun QR Code trouvé dans l'image.");
            }
        }
    }

    private String decodeQRCode(BufferedImage bufferedImage) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        Result result = new MultiFormatReader().decode(binaryBitmap, hints);
        return result.getText();
    }

    private void updateFieldsWithQRCodeData(String qrCodeData) {
         x=1;
        this. qrCodeParts = qrCodeData.split(",");
        for (int i = 0; i < qrCodeParts.length; i++) {
            System.out.println(qrCodeParts[i]);
        }
        if (qrCodeParts.length >= 6) {
            id.setText(qrCodeParts[0].trim());
numero_compteTF.setText(qrCodeParts[5].trim());
date_emissionTF.setText(qrCodeParts[4].trim());
emetteurTF1.setText(qrCodeParts[1].trim());
beneficiareTF1.setText(qrCodeParts[2].trim());
montantTF1.setText(qrCodeParts[3].trim());


         } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format QR Code non valide.");
        }

    }

    public void afficher(ActionEvent actionEvent) throws IOException {
        if (ajouterpage1 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage1, "/com/example/pi/afficherCheque.fxml");
        } else {
            System.err.println("Erreur : ajouterpage1 est null");
        }
    }

    public void retour(ActionEvent actionEvent) throws IOException {
        if (ajouterpage1 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage1, "/com/example/pi/modifier.fxml");
        } else {
            System.err.println("Erreur : ajouterpage1 est null");
        }
    }
}
