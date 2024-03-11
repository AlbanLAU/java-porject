package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class deleteVehicule {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        VehicleService vehicleService = context.getBean(VehicleService.class);


        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez l'id du véhicule à supprimer : ");
        Long id = sc.nextLong();
        sc.close();

        Vehicle vehicle = vehicleService.findById(id);

        if (vehicle == null) {
            System.out.println("Le véhicule n'existe pas.");
        } else {
            vehicleService.delete(vehicle);
            System.out.println("Le véhicule a bien été supprimé.");
        }
    }
}
