package com.example.pi.services;

import com.example.pi.models.cheque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.pi.utils.database.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;


public class chequeservice implements Iservice1<cheque> {
    private Connection connection;

    public chequeservice() {
        connection = getInstance().getConnection();
    }

    @Override
    public int ajouter(cheque cheque) throws SQLException {
        String req = "INSERT INTO cheque (montant, beneficiaire, emetteur, statut, date_emission, numerocompte) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(req, RETURN_GENERATED_KEYS)) {
            statement.setFloat(1, cheque.getMontant());
            statement.setString(2, cheque.getBeneficiaire());
            statement.setString(3, cheque.getEmetteur());
            statement.setString(4, cheque.getStatut().name());
            statement.setString(5, cheque.getDate_emission());
            statement.setString(6, cheque.getNumero_compte());

            int rowsAffected = statement.executeUpdate();

           if (rowsAffected == 1) {
                // Récupérer le numéro de chèque généré automatiquement
                ResultSet generatedKeys = statement.getGeneratedKeys();
               if (generatedKeys.next()) {
                   return generatedKeys.getInt(1);
               }
           }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }


       return -1;
    }

    @Override
    public void modifier(cheque cheque) throws SQLException {
        String req = "UPDATE cheque SET " +
                "montant = ?, " +
                "beneficiaire = ?, " +
                "emetteur = ?, " +
                "statut = ?, " +
                "date_emission = ? " +
                "WHERE numero_de_cheque = ?";  // Modifier ici

        try (PreparedStatement statement = connection.prepareStatement(req)) {
            // Paramètres pour la mise à jour
            statement.setFloat(1, cheque.getMontant());
            statement.setString(2, cheque.getBeneficiaire());
            statement.setString(3, cheque.getEmetteur());
            statement.setObject(4, cheque.getStatut().name());
            statement.setString(5, cheque.getDate_emission());

            // Paramètre pour la clause WHERE
            statement.setInt(6, cheque.getNumero_de_cheque());  // Modifier ici

            // Exécution de la requête de mise à jour
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour le numéro de chèque : " + cheque.getNumero_de_cheque());
            } else {
                System.out.println("Aucune ligne mise à jour. Vérifiez le numéro de chèque : " + cheque.getNumero_de_cheque());
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }




    @Override
    public void supprimer(int numero_de_cheque) throws SQLException {
        final String DELETE_CHEQUE = "DELETE FROM cheque WHERE numero_de_cheque = ?";

        try (PreparedStatement statement = connection.prepareStatement(DELETE_CHEQUE)) {
            statement.setInt(1, numero_de_cheque);

            // Exécution de la requête de suppression
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Suppression réussie pour le numéro de chèque : " + numero_de_cheque);
            } else {
                System.out.println("Aucune ligne supprimée. Vérifiez le numéro de chèque : " + numero_de_cheque);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    @Override
    public List<cheque> recuperer() {
        List<cheque> listeCheques = new ArrayList<>();
        final String sql = "SELECT c.numero_de_cheque, c.montant, c.beneficiaire, c.emetteur, c.statut, c.date_emission, dc.numerocompte AS demandecheque_numero_compte " +
                "FROM cheque c " +
                "JOIN demandecheque dc ON c.numerocompte = dc.numerocompte";





        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int numero_de_cheque =resultSet.getInt("numero_de_cheque");
                float montant = resultSet.getFloat("montant");
                String beneficiaire = resultSet.getString("beneficiaire");
                String emetteur = resultSet.getString("emetteur");
                cheque.Statut statut = cheque.Statut.valueOf(resultSet.getString("statut"));
                String dateEmission = resultSet.getString("date_emission");
                String numerocompte_ = resultSet.getString("demandecheque_numero_compte");
                System.out.println("Numero de Cheque: " + numero_de_cheque);
                System.out.println("Montant: " + montant);

                cheque c = new cheque(numero_de_cheque,montant,numerocompte_,beneficiaire,emetteur,dateEmission,statut);

                            listeCheques.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des chèques : " + e.getMessage());
        }

        return listeCheques;
    }
    public cheque getLastCheque() {
        String query = "SELECT * FROM cheque ORDER BY numero_de_cheque DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int numero_de_cheque = resultSet.getInt("numero_de_cheque");
                float montant = resultSet.getFloat("montant");
                String beneficiaire = resultSet.getString("beneficiaire");
                String emetteur = resultSet.getString("emetteur");
                cheque.Statut statut = cheque.Statut.valueOf(resultSet.getString("statut"));
                String dateEmission = resultSet.getString("date_emission");
                String numero_compte = resultSet.getString("numerocompte");

                return new cheque(numero_de_cheque,montant,numero_compte,beneficiaire,emetteur,dateEmission,statut);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle or log the exception as needed
        }

        return null;  // Or any default value indicating failure
    }

    public cheque getChequeByNumeroCheque(int numeroCheque) {
        final String SELECT_CHEQUE_BY_NUMERO = "SELECT * FROM cheque WHERE numero_de_cheque = ?";

        try (PreparedStatement statement = connection.prepareStatement(SELECT_CHEQUE_BY_NUMERO)) {
            statement.setInt(1, numeroCheque);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int numero_de_cheque = resultSet.getInt("numero_de_cheque");
                float montant = resultSet.getFloat("montant");
                String beneficiaire = resultSet.getString("beneficiaire");
                String emetteur = resultSet.getString("emetteur");
                String statut = resultSet.getString("statut");
                String dateEmission = resultSet.getString("date_emission");
                String numerocompte = resultSet.getString("numerocompte");

                return new cheque( montant, beneficiaire, emetteur, cheque.Statut.valueOf(statut), dateEmission, numerocompte);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du chèque : " + e.getMessage());
        }

        return null;
    }

}