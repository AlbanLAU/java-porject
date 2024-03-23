package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private ClientDao clientDao;

	private ClientService(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	
	public long create(Client client) throws ServiceException {
		try {
			return clientDao.create(client);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			return clientDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public void delete(Client client) throws ServiceException {
		try {
			clientDao.delete(client);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

	public Number count() throws ServiceException {
		try {
			List<Client> clientList = clientDao.findAll();
			return clientList.size();
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	
}
