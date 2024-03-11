package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class listReservation {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        ReservationService reservationService =  context.getBean(ReservationService.class);

        reservationService.findAll().forEach(reservation -> System.out.println(reservation));
    }
}
