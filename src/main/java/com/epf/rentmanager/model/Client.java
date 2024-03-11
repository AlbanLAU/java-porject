package com.epf.rentmanager.model;

import java.time.LocalDate;


public record Client(Long id, String nom, String prenom, String email, LocalDate naissance) {

    public Client() {
        this(null, null, null, null, LocalDate.now());
    }
    public Client(Long id, String nom, String prenom){
        this(id, nom, prenom, null, LocalDate.now());
    }

    public Client(Client client) {
        this(client.id, client.nom, client.prenom, client.email, client.naissance);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", naissance=" + naissance +
                '}';
    }
}

