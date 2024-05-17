package com.example.pi.controllers;

import com.example.pi.models.SwitchScene;
import com.example.pi.models.demandecheque;
import com.example.pi.services.demandechequeservice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.pi.controllers.Modifierdemande.showAlert;

public class Activationdemande implements Initializable {
    @FXML
    private ListView<demandecheque> listview;
    @FXML
    private AnchorPane ajouterpage4;


    private final demandechequeservice dc = new demandechequeservice();
List<demandecheque> alldemands=dc.recuperer();
    @FXML
    public boolean activateRequest(ActionEvent actionEvent) {
        demandecheque demandecheque = listview.getSelectionModel().getSelectedItem();
        System.out.println(demandecheque);
        if (demandecheque != null) {
            // Mettez à jour le statut de la demande à "Activé"
            demandecheque.setStatut_demande("Activé");

            try {
                // Mettez à jour la demande dans la base de données
                dc.modifier(demandecheque);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La demande a été activée avec succès");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'activation de la demande : " + e.getMessage());
            }
        }
        return true;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisez votre liste des chèques
        alldemands = dc.recuperer();

        // Triez la liste des chèques par numéro de chèque
        alldemands.sort(Comparator.comparing(demandecheque::getDate_demande));

        // Configurez la cellule personnalisée
        listview.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(demandecheque item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatdemandeCheque(item));
                }
            }

            private String formatdemandeCheque(demandecheque dc) {
                // Formattez les informations de chèque comme vous le souhaitez
                return String.format("numerocompte : %s, raison : %s, type_cheque : %s, tel : %s, cin : %s, date_demande : %s, montant_demandé : %.2f,statut_demande : %s",
                        dc.getNumero_compte(), dc.getRaison(), dc.getType_cheque(), dc.getTel(), dc.getCin(), dc.getDate_demande(), dc.getMontant_demandé(), dc.getStatut_demande());
            }


            // Autres méthodes du contrôleur...
        });
        listview.getItems().addAll(alldemands);
    }

    @FXML
    public void refuseRequest(ActionEvent actionEvent) {
        demandecheque selectedRequest = listview.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            // Mettez à jour le statut de la demande à "Refusé"
            selectedRequest.setStatut_demande("Refusé");

            try {
                // Mettez à jour la demande dans la base de données
                dc.modifier(selectedRequest);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "La demande a été refusée avec succès");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du refus de la demande : " + e.getMessage());
            }
        }
    }


    public void retourner(ActionEvent actionEvent) throws IOException {
        if (ajouterpage4 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage4, "/com/example/pi/ajouterdemande.fxml");
        } else {
            System.err.println("Erreur : ajouterpage1 est null");
        }
    }
}
