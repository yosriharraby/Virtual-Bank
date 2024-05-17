package com.example.pi.services;

import java.sql.SQLException;
import java.util.List;

public interface Iservice<demandecheque> {
    void ajouter (demandecheque demande) throws SQLException;
    void modifier (demandecheque demande) throws SQLException;
    void supprimer (String numéro_compte)throws SQLException;
    List<demandecheque>recuperer();

}
