package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class creerVehicule {
    public static void main(String[] args) throws DaoException, ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        VehicleService vehicleService = context.getBean(VehicleService.class);

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le constructeur du véhicule : ");
        String constructeur = sc.nextLine();
        System.out.println("Entrez le modèle du véhicule : ");
        String modele = sc.nextLine();
        System.out.println("Entrez le nombre de places du véhicule : ");
        int nbPlaces = sc.nextInt();
        sc.close();
        Vehicle vehicle = new Vehicle(constructeur, modele, nbPlaces);
        vehicleService.create(vehicle);
    }
}
