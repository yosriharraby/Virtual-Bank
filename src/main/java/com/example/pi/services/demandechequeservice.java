package com.example.pi.services;

import com.example.pi.models.demandecheque;
import com.example.pi.utils.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class demandechequeservice implements Iservice<demandecheque> {
    private Connection connection;

    public demandechequeservice() {
        connection = database.getInstance().getConnection();
    }

    public void ajouter(demandecheque demande) throws SQLException {
        String req = "INSERT INTO demandecheque (numerocompte, montant_demandé, raison, date_demande, cin, num_telephone, type_cheque, statut_demande) " +
                "VALUES('" + demande.getNumero_compte() + "','" + demande.getMontant_demandé() + "','" +
                demande.getRaison() + "','" + demande.getDate_demande() + "','" + demande.getCin() + "','" +
                demande.getTel() + "','" + demande.getType_cheque() + "','" + demande.getStatut_demande() + "')";

        // Exécutez votre requête ici, par exemple, à l'aide d'une PreparedStatement
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(req);
        }
    }

    @Override
    public void modifier(demandecheque demande) throws SQLException {
        String req = "UPDATE demandecheque SET " +
                "montant_demandé = '" + demande.getMontant_demandé() + "', " +
                "raison = '" + demande.getRaison() + "', " +
                "date_demande = '" + demande.getDate_demande() + "', " +
                "cin = '" + demande.getCin() + "', " +
                "num_telephone = '" + demande.getTel() + "', " +
                "type_cheque = '" + demande.getType_cheque() + "', " +  // Ajout de la virgule manquante ici
                "statut_demande = '" + demande.getStatut_demande() + "' " +
                "WHERE numerocompte = '" + demande.getNumero_compte() + "'";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.executeUpdate();
            System.out.println("Modification réussie");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(String numéro_compte) {
        final String DELETE_DEMANDE = "DELETE FROM demandecheque WHERE numéro_compte = ?";

        try (PreparedStatement statement = connection.prepareStatement(DELETE_DEMANDE)) {
            statement.setString(1, numéro_compte);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Suppression réussie pour le numéro de compte : " + numéro_compte);
            } else {
                System.out.println("Aucune ligne supprimée. Vérifiez le numéro de compte : " + numéro_compte);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    public List<demandecheque> recuperer() {
        List<demandecheque> demandesCheque = new ArrayList<>();
        final String GET_ALL_DEMANDES = "SELECT * FROM demandecheque";

        try (PreparedStatement ps = connection.prepareStatement(GET_ALL_DEMANDES);
             ResultSet result = ps.executeQuery()) {

            while (result.next()) {
                String numerocompte=result.getString("numerocompte");
                String cin = result.getString("cin");
                float montant_demandé = result.getFloat("montant_demandé");
                String num_telephone = result.getString("num_telephone");
                String raison = result.getString("raison");
                String type_cheque = result.getString("type_cheque");
                String date_demande= result.getString("date_demande");
                String statut_demande=result.getString("statut_demande");
                demandecheque demandeCheque = new demandecheque(numerocompte, raison, type_cheque, num_telephone, cin, date_demande, montant_demandé,statut_demande);

                demandeCheque.setNumero_compte(numerocompte);
                demandeCheque.setMontant_demandé(montant_demandé);
                demandeCheque.setRaison(raison);



                demandeCheque.setDate_demande(date_demande);

                demandeCheque.setCin(cin);
                demandeCheque.setTel(num_telephone);
                demandeCheque.setType_cheque(type_cheque);
                demandeCheque.setStatut_demande(statut_demande);
                demandesCheque.add(demandeCheque);
            }
        } catch (SQLException ex) {
            // Vous pouvez ajuster la gestion de l'exception selon vos besoins
            throw new RuntimeException("Erreur lors de la récupération des demandes : " + ex.getMessage());
        }

        return demandesCheque;
    }
    public String getStatut_demande(String numéro_compte) {
        String statut_demande = "";
        final String GET_STATUT_DEMANDE = "SELECT statut_demande FROM demandecheque WHERE numerocompte = ?";

        try (PreparedStatement ps = connection.prepareStatement(GET_STATUT_DEMANDE)) {
            ps.setString(1, numéro_compte);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                statut_demande = result.getString("statut_demande");
            }
        } catch (SQLException ex) {
            // Handle the exception appropriately (logging, throwing a custom exception, etc.)
            ex.printStackTrace();
        }

        return statut_demande;
    }
    public float getMontantDemandeByNumCompte(String numerocompte) {
        String query = "SELECT montant_demandé FROM demandecheque WHERE numerocompte = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, numerocompte);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getFloat("montant_demandé");
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception as needed
        }

        return 0; // Or any default value indicating failure
    }

}
