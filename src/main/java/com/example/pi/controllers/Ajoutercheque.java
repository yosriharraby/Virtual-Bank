package com.example.pi.controllers;

import com.example.pi.models.QRCodeGenerator;
import com.example.pi.models.SwitchScene;
import com.example.pi.models.cheque;
import com.example.pi.services.chequeservice;
import com.example.pi.services.demandechequeservice;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

public class Ajoutercheque {

    @FXML
    private TextField beneficiareTF;

    @FXML
    private TextField date_emissionTF;

    @FXML
    private TextField emetteurTF;

    @FXML
    private TextField montantTF;

    @FXML
    private ImageView qrcode;

    @FXML
    private AnchorPane ajouterPage;

    @FXML
    private TextField numéro_compteTF;

    private final chequeservice cs = new chequeservice();

    private final demandechequeservice dc = new demandechequeservice();
    private final Activationdemande activationdemandeController = new Activationdemande();

    private String qrCodeData;
    private String content;
    private String[] qrCodeParts;


    @FXML
    void affichercheque(ActionEvent event) throws IOException {
        SwitchScene ss = new SwitchScene(ajouterPage, "/com/example/pi/afficherCheque.fxml");
    }

    @FXML
    void ajoutercheque(ActionEvent event) throws SQLException {
        if (controlSaisie(numéro_compteTF) && controlSaisie(emetteurTF) && controlSaisie(beneficiareTF) &&
                controlSaisie(montantTF) && controlSaisie(date_emissionTF)) {
            if (controlSaisieDate(date_emissionTF, "date émission") && controlSaisieInt(numéro_compteTF, "numéro compte")) {
                float montant = Float.parseFloat(montantTF.getText());
                String numerocompte = numéro_compteTF.getText();

                String statut_demande = dc.getStatut_demande(numerocompte);

                if ("Activé".equals(statut_demande) && montant <= dc.getMontantDemandeByNumCompte(numerocompte)) {
                    // Déclaration de la variable newCheque avant son utilisation
                    cheque newCheque = new cheque(montant,beneficiareTF.getText(),
                            emetteurTF.getText(), cheque.Statut.ENCAISSE,
                            date_emissionTF.getText(),numerocompte);

                    cs.ajouter(newCheque);
cheque lastcheque=cs.getLastCheque();
                    showAlert(Alert.AlertType.INFORMATION, "succes",
                            "cheque  ajoute avec succes");
                    String uniqueFileName = generateUniqueFileName(newCheque);
                    String qrCodePath = "C:\\Users\\Lenovo\\IdeaProjects\\pi\\src\\main\\resources\\images\\"
                            + uniqueFileName + ".png";






                    String qrCodeText = lastcheque.getNumero_de_cheque() + "," +
                            lastcheque.getBeneficiaire()+ "," +
                            lastcheque.getEmetteur()  + "," +
                           lastcheque.getMontant() + "," +
                            lastcheque.getDate_emission() +","+lastcheque.getNumero_compte();
                    QRCodeGenerator.generateQRCode(qrCodeText, 300, 300, qrCodePath);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur",
                            "La demande n'est pas activée ou le montant demandé n'est pas suffisant.");
                }
            }
        }
    }

    private String generateUniqueFileName(cheque newCheque) {
        return "qr_" + newCheque.getDate_emission();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    public boolean controlSaisieInt(TextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText());
            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", fieldName + " doit être un nombre entier");
            return false;
        }
    }

    public boolean controlSaisie(TextField field) {
        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "champ vide", "remplir le champ vides");
            return false;
        }
        return true;
    }

    @FXML
    public void scanner(ActionEvent actionEvent) {
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
                showAlert(Alert.AlertType.valueOf("erreur"),"erreur lors de chargement","QR non valide");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
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

    private void updateFieldsWithQRCodeData(String result) {
        String[] qrCodeParts = result.split(",");

        if (qrCodeParts.length >= 5) {
            int numeroCheque = Integer.parseInt(qrCodeParts[0].trim());
            beneficiareTF.setText(qrCodeParts[1].trim());
            emetteurTF.setText(qrCodeParts[2].trim());
            montantTF.setText(qrCodeParts[3].trim());
            date_emissionTF.setText(qrCodeParts[4].trim());
        } else {
            showAlert(Alert.AlertType.valueOf("erreur"), "erreur lors de chargement", "QR non valide");
        }
    }


    public void modif(ActionEvent actionEvent) throws IOException {
        if (ajouterPage != null) {
            SwitchScene ss = new SwitchScene(ajouterPage, "/com/example/pi/modifier.fxml");
        } else {
            System.err.println("Erreur : ajouterpage1 est null");
        }

    }
}