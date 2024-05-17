package com.example.pi.test;

import com.example.pi.models.cheque;
import com.example.pi.services.chequeservice;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class main2 {
    public static void main(String[] args) throws ParseException, SQLException {
        chequeservice cs = new chequeservice();
        List<cheque> s=cs.recuperer();
        System.out.println(s);


        // Utilisez java.sql.Date pour représenter la date dans le format MySQL


        // Ajouter un chèque
      // cs.ajouter(new cheque(123, 500.0f, "John Doe", "Company XYZ", "En attente", sqlDate));
     //  cs.ajouter(new cheque(127, 1345.500f, "JUlIEN", "Company Aramex", cheque.Statut.ENCAISSE, "12", numéro_compte));
        // Modifier un chèque (remarque : cette opération nécessitera probablement une méthode spécifique pour modifier)
         //cs.modifier(new cheque(127, 1500.14f, "JULIEN", "meublatex", "En attente", sqlDate));
        // Supprimer un chèque
        //cs.supprimer("127");

        // Récupérer la liste des chèques
     //  List<cheque> recuperer = cs.recuperer();
      //  for (cheque c : recuperer) {
           // System.out.println(c.toString());
        //}
   }
}

