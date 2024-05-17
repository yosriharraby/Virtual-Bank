package com.example.pi.services;

import java.sql.SQLException;
import java.util.List;

public interface Iservice1 <cheque> {
    int ajouter (cheque cheque) throws SQLException;
    void modifier (cheque cheque) throws SQLException;
    void supprimer (int numero_de_cheque)throws SQLException;
    List<cheque> recuperer();

}
