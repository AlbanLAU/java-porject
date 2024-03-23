package com.epf.rentmanager.ui.servlet.vehicule;

import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cars/create")
public class VehiculeCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

     @Autowired
     VehicleService vehiculeService;

     @Override
     public void init() throws ServletException {
         super.init();
         SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
     }

     protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {

         this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
     }
}
