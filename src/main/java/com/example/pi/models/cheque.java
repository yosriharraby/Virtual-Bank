package com.example.pi.models;

public class cheque {

   private int numero_de_cheque;
   private float montant;
   private String numero_compte;



   public enum Statut {
      EN_ATTENTE,
      ENCAISSE,

      }
   private String beneficiaire, emetteur, date_emission;
   private Statut statut;  // Utiliser le même nom que l'enum


   public cheque(int numero_de_cheque, float montant, String numero_compte, String beneficiaire, String emetteur, String date_emission, Statut statut) {
      this.numero_de_cheque = numero_de_cheque;
      this.montant = montant;
      this.numero_compte = numero_compte;
      this.beneficiaire = beneficiaire;
      this.emetteur = emetteur;
      this.date_emission = date_emission;
      this.statut = statut;
   }

   public cheque(int numero_de_cheque, float montant, String beneficiare, String emetteur, String date_emission, String numero_compte) {

      this.montant = montant;
      this.beneficiaire = beneficiare;
      this.emetteur = emetteur;
      this.statut = statut;
      this.date_emission = date_emission;
      this.numero_compte = this.numero_compte;
   }





   public cheque(float montant, String beneficiaire, String emetteur, Statut statut, String date_emission, String numero_compte) {
      this.montant = montant;
      this.beneficiaire = beneficiaire;
      this.emetteur = emetteur;
      this.statut = statut;
      this.date_emission = date_emission;
      this.numero_compte = numero_compte;
   }

   public int getNumero_de_cheque() {
      return numero_de_cheque;
   }

   public void setNumero_de_cheque(int numero_de_cheque) {
      this.numero_de_cheque = numero_de_cheque;
   }

   public float getMontant() {
      return montant;
   }

   public void setMontant(float montant) {
      this.montant = montant;
   }

   public String getNumero_compte() {
      return numero_compte;
   }

   public void setNumero_compte(String numero_compte) {
      this.numero_compte = numero_compte;
   }

   public String getBeneficiaire() {
      return beneficiaire;
   }

   public void setBeneficiaire(String beneficiaire) {
      this.beneficiaire = beneficiaire;
   }

   public String getEmetteur() {
      return emetteur;
   }

   public void setEmetteur(String emetteur) {
      this.emetteur = emetteur;
   }

   public String getDate_emission() {
      return date_emission;
   }

   public void setDate_emission(String date_emission) {
      this.date_emission = date_emission;
   }

   public Statut getStatut() {
      return statut;
   }

   public void setStatut(Statut statut) {
      this.statut = statut;
   }

   @Override
   public String toString() {
      return "cheque{" +
              "numero_de_cheque=" + numero_de_cheque +
              ", montant=" + montant +
              ", beneficiaire='" + beneficiaire + '\'' +
              ", emetteur='" + emetteur + '\'' +
              ", statut='" + statut + '\'' +
              ", date_emission=" + date_emission +
              '}';
   }

   public void encaisser() {
      if (this.statut == Statut.EN_ATTENTE) {
         this.statut = Statut.ENCAISSE;
         System.out.println("Chèque encaissé avec succès.");
      } else {
         System.out.println("Impossible d'encaisser le chèque. Statut actuel : " + this.statut);
      }
   }
}
