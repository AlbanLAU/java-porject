package com.epf.rentmanager.model;

public record Vehicle(Long id, String constructeur, String modele, int nbPlaces) {

    public Vehicle() {
        this(null, null, null, 2);
    }

    public Vehicle(String constructeur, String modele, int nbPlaces){
        this(null, constructeur, modele, nbPlaces);
    }
    public Vehicle(Vehicle vehicle) {
        this(vehicle.id, vehicle.constructeur, vehicle.modele, vehicle.nbPlaces);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", constructeur='" + constructeur + '\'' +
                ", modele='" + modele + '\'' +
                ", nbPlaces=" + nbPlaces +
                '}';
    }
}
