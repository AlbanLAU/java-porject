package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class deleteClient {
    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        ClientService clientService = context.getBean(ClientService.class);



        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez l'id du client à supprimer : ");
        Long id = sc.nextLong();
        sc.close();

        Client client = clientService.findById(id);

        if (client == null) {
            System.out.println("Le client n'existe pas.");
        } else {
            clientService.delete(client);
            System.out.println("Le client a bien été supprimé.");
        }
    }
}
