package com.epf.rentmanager.model;

import java.time.LocalDate;

public record Reservation(Long id, Long clientId, Long vehicleId, LocalDate debut, LocalDate fin) {

    public Reservation() {
        this(null, null, null, LocalDate.now(), LocalDate.now());
    }


    public Reservation(Reservation reservation) {
        this(reservation.id, reservation.clientId, reservation.vehicleId, reservation.debut, reservation.fin);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", vehicleId=" + vehicleId +
                ", debut='" + debut + '\'' +
                ", fin='" + fin + '\'' +
                '}';
    }
}
