package com.example.pi.controllers;

import com.example.pi.models.SwitchScene;
import com.example.pi.models.demandecheque;
import com.example.pi.services.demandechequeservice;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Afficherdemandes implements Initializable {

    @FXML
    private ListView<demandecheque> listview;
    @FXML
    private MFXTextField searchField;
    @FXML
    private AnchorPane ajouterpage2;


    private final demandechequeservice dc = new demandechequeservice();
    List<demandecheque> alldemands = dc.recuperer();

    public void supprimerdemande(ActionEvent actionEvent) throws SQLException {
        demandecheque demandecheque = listview.getSelectionModel().getSelectedItem();
        if (demandecheque != null) {
            dc.supprimer(demandecheque.getNumero_compte());
            listview.getItems().remove(demandecheque);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("demande Cheque est supprimee");
            alert.setContentText("demande Cheque est supprimee");
            alert.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selecter un Cheque");
            alert.setContentText("selecter une Cheque");
            alert.show();
        }

    }

    public void modifierDemandes(ActionEvent actionEvent) throws IOException {
        demandecheque demandecheque = listview.getSelectionModel().getSelectedItem();
        if (demandecheque != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pi/modifierdemande.fxml"));
            Parent root = loader.load();
            Modifierdemande modifierDemande = loader.getController();
            modifierDemande.initData(demandecheque);
            modifierDemande.setC(demandecheque);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("tu dois selectionner une demande ! ");
            alert.show();
        }

    }

    @Override
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
                return String.format("numéro_compte : %s, raison : %s, type_cheque : %s, tel : %s, cin : %s, date_demande : %s, montant_demandé : %.2f,statut_demande : %s",
                        dc.getNumero_compte(), dc.getRaison(), dc.getType_cheque(), dc.getTel(), dc.getCin(), dc.getDate_demande(), dc.getMontant_demandé(), dc.getStatut_demande());
            }


            // Autres méthodes du contrôleur...
        });
        listview.getItems().addAll(alldemands);
    }

    @FXML
    void rechercherDemandes(ActionEvent event) {
        String recherche = searchField.getText();

        // Filtrez les chèques en fonction du critère de recherche
        List<demandecheque> demandechèquesFiltrés = alldemands.stream()
                .filter(dc -> matchCritere(dc, recherche))
                .collect(Collectors.toList());

        // Mettez à jour la ListView avec les chèques filtrés
        listview.getItems().setAll(demandechèquesFiltrés);
    }

    private boolean matchCritere(demandecheque dc, String recherche) {
        // Ajoutez ici la logique pour comparer le chèque en fonction de divers critères
        return dc.getNumero_compte().contains(recherche) || dc.getTel().contains(recherche)
                || dc.getCin().contains(recherche) || dc.getType_cheque().contains(recherche)
                || dc.getDate_demande().contains(recherche) || dc.getRaison().contains(recherche)
                || String.format("%.2f", dc.getMontant_demandé()).contains(recherche);
    }


    public void gererdemandes(ActionEvent actionEvent) throws IOException {
        if (ajouterpage2 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage2, "/com/example/pi/activationdemande.fxml");
        } else {
            // Faites quelque chose en cas de null, par exemple affichez un message d'erreur.
            System.err.println("Erreur : ajouterpage2 est null");
        }
    }

    public void ajouter(ActionEvent actionEvent) throws IOException {
        if (ajouterpage2 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage2, "/com/example/pi/ajouterdemande.fxml");
        } else {
            // Faites quelque chose en cas de null, par exemple affichez un message d'erreur.
            System.err.println("Erreur : ajouterpage2 est null");
        }
    }
}