package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    private ReservationService(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }

    public long create(Reservation client) throws ServiceException {
        try {
            return reservationDao.create(client);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findResaByClientId(long id) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findResaByVehicleId(long id) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void delete(Reservation reservation) throws ServiceException {
        try {
            reservationDao.delete(reservation);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
