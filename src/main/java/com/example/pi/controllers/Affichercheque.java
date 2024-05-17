package com.example.pi.controllers;

import com.example.pi.models.SwitchScene;
import com.example.pi.models.cheque;
import com.example.pi.services.chequeservice;
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

public class Affichercheque implements Initializable {

    @FXML
    private MFXTextField searchField;
    @FXML
    private ListView<cheque> listview;
    @FXML
    private AnchorPane ajouterpage3;


    private final chequeservice cs = new chequeservice();
    List<cheque> allCheque = cs.recuperer();


    @FXML
    void supprimerCheque(ActionEvent event) throws SQLException {
        cheque cheque = listview.getSelectionModel().getSelectedItem();
        if (cheque != null){
            cs.supprimer(cheque.getNumero_de_cheque());
            listview.getItems().remove(cheque);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Cheque est supprimee");
            alert.setContentText("Cheque est supprimer");
            alert.show();

        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selecter un Cheque");
            alert.setContentText("selecter une Cheque");
            alert.show();
        }

    }
    @FXML
    void modifierCheque(ActionEvent event) throws IOException {
        cheque cheque = listview.getSelectionModel().getSelectedItem();
        System.out.println(cheque);
        if(cheque != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pi/modifier.fxml"));
            Parent root = loader.load();
            Modifier modifierCheque = loader.getController();
            modifierCheque.initData(cheque);
            modifierCheque.setC(cheque);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("tu dois selectionner un cheque ! ");
            alert.show();
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisez votre liste des chèques
        allCheque = cs.recuperer();

        // Triez la liste des chèques par numéro de chèque
        allCheque.sort(Comparator.comparing(cheque::getDate_emission));

        // Configurez la cellule personnalisée
        listview.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
                    @Override
                    protected void updateItem(cheque item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(formatCheque(item));
                        }
                    }

                    private String formatCheque(cheque c) {
                        // Formattez les informations de chèque comme vous le souhaitez
                        return String.format("Numéro de chèque : %d, Montant : %.2f, Bénéficiaire : %s, Emetteur : %s, Statut : %s, Date d'émission : %s, numerocompte : %s",
                                c.getNumero_de_cheque(), c.getMontant(), c.getBeneficiaire(), c.getEmetteur(), c.getStatut(), c.getDate_emission(),c.getNumero_compte());
                    }


                    // Autres méthodes du contrôleur...
                });
                listview.getItems().addAll(allCheque);
    }
    @FXML
    void rechercherCheques(ActionEvent event) {
        String recherche = searchField.getText();

        // Filtrez les chèques en fonction du critère de recherche
        List<cheque> chèquesFiltrés = allCheque.stream()
                .filter(c -> matchCritere(c, recherche))
                .collect(Collectors.toList());

        // Mettez à jour la ListView avec les chèques filtrés
        listview.getItems().setAll(chèquesFiltrés);
    }
    private boolean matchCritere(cheque c, String recherche) {
        // Ajoutez ici la logique pour comparer le chèque en fonction de divers critères
        return String.valueOf(c.getNumero_de_cheque()).contains(recherche) ||
                String.format("%.2f", c.getMontant()) .contains(recherche)||
                c.getBeneficiaire().contains(recherche) ||
                c.getEmetteur().contains(recherche) ||
                c.getStatut().toString().contains(recherche) ||
                c.getDate_emission().contains(recherche);
    }

    public void ajouter(ActionEvent actionEvent) throws IOException {
        if (ajouterpage3 != null) {
            SwitchScene ss = new SwitchScene(ajouterpage3, "/com/example/pi/ajoutercheque.fxml");
        } else {
            // Faites quelque chose en cas de null, par exemple affichez un message d'erreur.
            System.err.println("Erreur : ajouterpage3 est null");
        }
    }
}