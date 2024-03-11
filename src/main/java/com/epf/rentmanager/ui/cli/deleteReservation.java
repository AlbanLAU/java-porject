package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class deleteReservation {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        ReservationService reservationService =  context.getBean(ReservationService.class);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the reservation's id: ");
        Long id = scanner.nextLong();
        scanner.close();

        Reservation reservation = reservationService.findById(id);

        if (reservation == null) {
            System.out.println("The reservation does not exist.");
        } else {
            reservationService.delete(reservation);
            System.out.println("The reservation has been deleted.");
        }
    }
}
