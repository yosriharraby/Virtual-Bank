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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static com.example.pi.controllers.Modifierdemande.showAlert;

public class Ajouterdemande {
    @FXML
    private TextField cinTF;
    @FXML
    private TextField date_demandeTF;

    @FXML
    private TextField montant_demandéTF;

    @FXML
    private TextField num_telephoneTF;
    @FXML
    private TextField numero_compteTF;

    @FXML
    private TextArea raisonTF;

    @FXML
    private TextField typechequeTF;
    @FXML
    private AnchorPane ajouterPage1;


    private final demandechequeservice dc = new demandechequeservice();

    public boolean controlSaisie (TextField field){
        if (field.getText().isEmpty() ) {
            showAlert(Alert.AlertType.ERROR, "champ vide", "remplir le champ vides");
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
    public boolean controlSaisieTypeCheque(TextField field) {
        String[] allowedTypes = {"barre", "nominatif", "certifie"};
        String fieldType = field.getText().toLowerCase();

        if (!Arrays.asList(allowedTypes).contains(fieldType)) {
            showAlert(Alert.AlertType.ERROR, "Type de chèque non valide", "Le type de chèque doit être barré, nominatif ou certifié");
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
    void ajouterDemande() throws SQLException, IOException {
        if (controlSaisie(montant_demandéTF) && controlSaisie(num_telephoneTF) &&
                controlSaisie(typechequeTF) &&  controlSaisie(numero_compteTF)&&controlSaisie(date_demandeTF)) {
            if (controlSaisieInt(numero_compteTF, "numéro compte") && controlSaisieInt(num_telephoneTF, "tel")&&controlSaisieInt(cinTF,"cin")
                    && controlSaisieTypeCheque(typechequeTF)&&controlSaisieDate(date_demandeTF, "Date demande") ) {
                // Conversion de la chaîne montant_demandéTF en float
                float montant1 = Float.parseFloat(montant_demandéTF.getText());

                // Conversion de la chaîne de texte de la date en java.sql.Date
                dc.ajouter(new demandecheque(numero_compteTF.getText(), raisonTF.getText(), typechequeTF.getText(), num_telephoneTF.getText(), cinTF.getText(), date_demandeTF.getText(), montant1,"en attente"));
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La demande de chèque a été deposée avec succès");
            }
        }
    }

    public void afficherdemande(ActionEvent actionEvent) throws IOException {
        SwitchScene ss = new SwitchScene(ajouterPage1,"/com/example/pi/afficherdemandes.fxml");
    }
}