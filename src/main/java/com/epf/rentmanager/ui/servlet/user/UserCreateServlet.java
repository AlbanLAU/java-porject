package com.epf.rentmanager.ui.servlet.user;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@WebServlet("/users/create")
public class UserCreateServlet extends HttpServlet{
    private static final long serialVersionUID = 1L;

    @Autowired
    ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("id") != null){
            try {
                long id = Long.parseLong(request.getParameter("id"));
                Client client = clientService.findById(id);
                request.setAttribute("client", client);
            } catch (ServiceException e){
                throw new RuntimeException(e);
            }
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

        String last_name = request.getParameter("last_name");
        String first_name = request.getParameter("first_name");
        String email = request.getParameter("email");
        String naissance_str = request.getParameter("naissance");

        if (last_name == null || first_name == null || email == null || naissance_str == null) {
            throw new ServletException("Missing parameter: last_name or first_name or email or naissance");
        }
        LocalDate naissance;

        if (naissance_str != null && !naissance_str.isEmpty()) {
            naissance = LocalDate.parse(naissance_str);

            if (request.getParameter("id") != null) {
                Client client = new Client(Long.parseLong(request.getParameter("id")), last_name, first_name, email, naissance);
                try {
                    clientService.update(client);
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Client client = new Client(0L, last_name, first_name, email, naissance);
                try {
                    clientService.create(client);
                } catch (ServiceException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/users/list");
    }
}