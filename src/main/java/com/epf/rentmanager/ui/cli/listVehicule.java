package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class listVehicule {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        VehicleService vehicleService = context.getBean(VehicleService.class);

        vehicleService.findAll().forEach(vehicle -> System.out.println(vehicle));
    }
}
