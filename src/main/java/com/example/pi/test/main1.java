package com.example.pi.test;

import com.example.pi.services.demandechequeservice;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class main1 {
    public static void main(String[] args) throws ParseException, SQLException {
        demandechequeservice dc = new demandechequeservice();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String dateInString = "7-Jun-2023";
        Date date = formatter.parse(dateInString);

        // Utilisez java.sql.Date pour représenter la date dans le format MySQL
       // java.sql.Date sqlDate = (date != null) ? new java.sql.Date(date.getTime()) : null;

        // Ajoutez une demande de chèque
       // dc.modifier(new demandecheque("01103248", "remboursement_dette", "barre", "55003047", "14417295", "14", 147.52f));

        //dc.modifier(new demandecheque("14417295","payement",sqlDate,"cin1",(float) 178.10f));
        //dc.supprimer("01103248");
        //List<demandecheque> demandesCheque = dc.recuperer();
        //for (demandecheque demande : demandesCheque) {
           // System.out.println(demande.toString());
       // }

    }
}

