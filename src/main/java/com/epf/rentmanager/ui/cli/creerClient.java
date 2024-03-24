package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.config.AppConfiguration;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class creerClient {
    public static void main(String[] args) throws DaoException, ServiceException {
        ApplicationContext context = new
                AnnotationConfigApplicationContext(AppConfiguration.class);
        ClientService clientService = context.getBean(ClientService.class);

        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le nom du client : ");
        String nom = sc.nextLine();

        System.out.println("Entrez le prénom du client : ");
        String prenom = sc.nextLine();

        System.out.println("Entrez l'email du client : ");
        String email = sc.nextLine();

        System.out.println("Entrez la date de naissance du client : ");
        System.out.println("le jour : ");
        Long jour = sc.nextLong();
        System.out.println("le mois : ");
        Long mois = sc.nextLong();
        System.out.println("l'année : ");
        Long annee = sc.nextLong();
        Date naissance = new Date(annee.intValue(), mois.intValue(), jour.intValue());

        sc.close();
        Client client = new Client(null, nom, prenom, email, naissance);
        clientService.create(client);
    }
}
