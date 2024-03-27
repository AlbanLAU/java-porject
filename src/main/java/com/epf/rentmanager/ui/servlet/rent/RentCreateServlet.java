package com.epf.rentmanager.ui.servlet.rent;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/rents/create")
public class RentCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ClientService clientService;

    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Client> clients = new ArrayList<>();
        List<Vehicle> vehicules = new ArrayList<>();

        try {
            vehicules = vehicleService.findAll();
            clients = clientService.findAll();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("vehicles", vehicules);
        request.setAttribute("clients", clients);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

        String car = request.getParameter("car");
        String client = request.getParameter("client");
        String begin = request.getParameter("begin");
        String end = request.getParameter("end");

        if (car == null || client == null || begin == null || end == null) {
            throw new ServletException("Missing parameter: car or client or begin or end");
        }

        try {
            reservationService.create(new Reservation(null, Long.parseLong(client), Long.parseLong(car), java.time.LocalDate.parse(begin), java.time.LocalDate.parse(end)));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect(request.getContextPath() + "/rent/list");
    }

}
