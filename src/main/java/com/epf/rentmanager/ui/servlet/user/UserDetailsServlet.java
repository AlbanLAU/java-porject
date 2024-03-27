package com.epf.rentmanager.ui.servlet.user;

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

@WebServlet("/users/details")
public class UserDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    ClientService clientService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Reservation> reservations = new ArrayList<>();
        List<Vehicle> vehicles = new ArrayList<>();
        Client client = null;

        try {
            reservations = reservationService.findResaByClientId(Long.parseLong(request.getParameter("id")));
            client = clientService.findById(Long.parseLong(request.getParameter("id")));
            for (Reservation resa : reservations) {
                Vehicle vehicle = vehicleService.findById(resa.vehicleId());
                if (vehicle != null && !vehicles.contains(vehicle)) {
                    vehicles.add(vehicle);
                }
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("client", client);
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("reservations", reservations);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
    }

}
