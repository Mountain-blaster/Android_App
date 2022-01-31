package com.project.webserviceapp.app.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.*;
import com.project.webserviceapp.app.config.Server;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Personne {

    @Expose
    @SerializedName("prenom")
    private String prenom;

    @Expose
    @SerializedName("nom")
    private String nom;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("dateNaissance")
    private String dateNaissance;

    @Expose
    @SerializedName("dateModification")
    private String dateModification;

    @Expose
    @SerializedName("dateEnregistrement")
    private String dateEnregistrement;

    @Expose
    @SerializedName("clef")
    private String clef;

    transient SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public Personne (String prenom, String nom, String email, Date naissance) throws Exception {
        setPrenom(prenom);
        setNom(nom);
        setEmail(email);
        setClef();
        setNaissance(naissance);
        setEnregistrement();
        setModification();
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setPrenom(String prenom) throws Exception { this.prenom = prenom; }

    public void setNom(String nom) throws Exception {
        if (nom.length() > 50)
            throw  new  Exception("Nom must not be more than 50 characters");

        this.nom = nom;
    }

    public void setEmail(String email) throws Exception {
        if (email.length() > 50)
            throw  new  Exception("Email must not be more than 50 characters");

        this.email = email;
    }

    public void setNaissance(Date naissance) throws Exception {

        this.dateNaissance = formatter.format(naissance);
    }

    public void setEnregistrement() {

        this.dateEnregistrement = formatter.format(new Date());
    }

    public void setModification() {

        this.dateModification = formatter.format(new Date());
    }

    public void setClef() { this.clef = Server.clef; }

    @Override
    public String toString() { return prenom + nom; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Personne personne = (Personne) o;

        return Objects.equals(prenom, personne.prenom) &&
                Objects.equals(nom, personne.nom) &&
                Objects.equals(email, personne.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prenom, nom, email, dateNaissance, dateEnregistrement, dateModification);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
