package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class creerReservation {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        ReservationService reservationService =  context.getBean(ReservationService.class);

        Scanner sc = new Scanner(System.in);

        System.out.println("Entrez l'id du véhicule : ");
        long vehicleId = sc.nextLong();
        System.out.println("Entrez l'id du client : ");
        long clientId = sc.nextLong();
        System.out.println("Entrez la date de début (YYYY-MM-DD) : ");
        String debut = sc.next();
        System.out.println("Entrez la date de fin (YYYY-MM-DD) : ");
        String fin = sc.next();
        sc.close();

        Reservation reservation = new Reservation(  null, clientId, vehicleId, java.time.LocalDate.parse(debut), java.time.LocalDate.parse(fin));
        reservationService.create(reservation);
    }
}
