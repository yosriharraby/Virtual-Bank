package com.example.pi.controllers;

import com.example.pi.models.SwitchScene;
import com.example.pi.models.demandecheque;
import com.example.pi.services.demandechequeservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Modifierdemande {

    @FXML
    private TextField cinTF1;

    @FXML
    private TextField date_demandeTF1;

    @FXML
    private AnchorPane modificationdemande;


    @FXML
    private TextField montant_demandéTF1;

    @FXML
    private TextField num_telephoneTF1;

    @FXML
    private TextArea raisonTF1;

    @FXML
    private TextField type_chequeTF1;

    @FXML
    private TextField numero_compteTF1;

    private final demandechequeservice dc = new demandechequeservice();

    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);

        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean controlSaisie(TextField field) {
        if (field.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "champ vide", "remplir le champ vides");
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

    public boolean controlSaisieTypeCheque(TextField field) {
        String[] allowedTypes = {"barré", "nominatif", "certifié"};
        String fieldType = field.getText().toLowerCase();

        if (!Arrays.asList(allowedTypes).contains(fieldType)) {
            showAlert(Alert.AlertType.ERROR, "Type de chèque non valide", "Le type de chèque doit être barré, nominatif ou certifié");
            return false;
        }

        return true;
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

    private demandecheque dc1;

    public void setC(demandecheque dc1) {
        this.dc1 = dc1;
    }

    public void initData(demandecheque dc1) {
        if (dc1 != null) {
            cinTF1.setText(String.valueOf(dc1.getCin()));
            date_demandeTF1.setText(dc1.getDate_demande());
            montant_demandéTF1.setText(String.valueOf(dc1.getMontant_demandé()));
            raisonTF1.setText(String.valueOf(dc1.getRaison()));
            type_chequeTF1.setText(String.valueOf(dc1.getType_cheque()));
            num_telephoneTF1.setText(String.valueOf(dc1.getTel()));
            numero_compteTF1.setText(String.valueOf(dc1.getNumero_compte()));
        }
    }


    /* @FXML
     void ok(ActionEvent event) throws SQLException {
         float montant2 = Float.parseFloat(montant_demandéTF1.getText());
         if(controlSaisie( date_demandeTF1)&&controlSaisie(  montant_demandéTF1)&&controlSaisie(  cinTF1)&&controlSaisie(   num_telephoneTF1)&&controlSaisie(   type_chequeTF1)){
             demandecheque dc1 = new demandecheque("00145148",raisonTF1.getText(),type_chequeTF1.getText(),num_telephoneTF1.getText(),cinTF1.getText(),montant2);
             dc.modifier(dc1);
             showAlert(Alert.AlertType.INFORMATION,"Succée","demande cheque est modifée");

         }
 */
    @FXML
    void ok(ActionEvent event) throws SQLException {

        if (controlSaisie(date_demandeTF1) && controlSaisie(montant_demandéTF1) && controlSaisie(cinTF1) &&
                controlSaisie(num_telephoneTF1) && controlSaisie(type_chequeTF1) &&controlSaisie(date_demandeTF1) ) {
            if (controlSaisieInt(numero_compteTF1, "numéro compte") && controlSaisieInt(num_telephoneTF1, "tel") && controlSaisieInt(cinTF1, "cin")
                    && controlSaisieTypeCheque(type_chequeTF1)&& controlSaisieDate(date_demandeTF1, "date demande")) {
                float montant2 = Float.parseFloat(montant_demandéTF1.getText());

                demandecheque dc1 = new demandecheque(numero_compteTF1.getText(), raisonTF1.getText(), type_chequeTF1.getText(),
                        num_telephoneTF1.getText(), cinTF1.getText(), date_demandeTF1.getText(), montant2,"en attente");
                dc.modifier(dc1);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La demande de chèque a été modifiée avec succès");
                Stage stage = (Stage) modificationdemande.getScene().getWindow();
                stage.close();
            }
        }
    }

    public void afficher(ActionEvent actionEvent) throws IOException {
        if (modificationdemande != null) {
            SwitchScene ss = new SwitchScene(modificationdemande, "/com/example/pi/afficherdemandes.fxml");
        } else {
            // Faites quelque chose en cas de null, par exemple affichez un message d'erreur.
            System.err.println("Erreur : modificationdemande est null");
        }
    }
}
