package com.example.pi.models;

public class demandecheque {
    private String numero_compte;
    private String raison;
    private String type_cheque;
    private String tel;
    private String date_demande;
    private String cin;
    private String statut_demande;
    private float montant_demandé;


    public demandecheque(String numero_compte, String raison, String type_cheque, String tel, String cin, String date_demande, float montant_demandé, String statut_demande) {
        this.numero_compte = numero_compte;
        this.raison = raison;
        this.type_cheque = type_cheque;
        this.tel = tel;
        this.cin = cin;
        this.date_demande = date_demande;
        this.montant_demandé = montant_demandé;
        this.statut_demande=statut_demande;
    }

    public String getNumero_compte() {
        return numero_compte;
    }

    public String getRaison() {
        return raison;
    }

    public String getDate_demande() {
        return date_demande;
    }



    public float getMontant_demandé() {
        return montant_demandé;
    }

    public void setNumero_compte(String numero_compte) {
        this.numero_compte = numero_compte;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public void setDate_demande(String date_demande) {
        this.date_demande = date_demande;
    }


    public void setMontant_demandé(float montant_demandé) {
        this.montant_demandé = montant_demandé;
    }

    public String getType_cheque() {
        return type_cheque;
    }

    public void setType_cheque(String type_cheque) {
        this.type_cheque = type_cheque;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getStatut_demande() {
        return statut_demande;
    }

    public void setStatut_demande(String statut_demande) {
        this.statut_demande = statut_demande;
    }

    @Override
    public String toString() {
        return "demandecheque{" +
                "numéro_compte='" + numero_compte + '\'' +
                ", raison='" + raison + '\'' +
                ", type_cheque='" + type_cheque + '\'' +
                ", tel='" + tel + '\'' +
                ", date_demande='" + date_demande + '\'' +
                ", cin='" + cin + '\'' +
                ", statut_demande='" + statut_demande + '\'' +
                ", montant_demandé=" + montant_demandé +
                '}';
    }
}
