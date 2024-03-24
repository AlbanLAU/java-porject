package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private ClientDao() {}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public long create(Client client) throws DaoException, ServiceException {
		if (client.nom() == null || client.prenom() == null ){
			throw new ServiceException("Le nom et prénom du client ne peuvent pas être null.");
		} else {
			try (Connection connection = ConnectionManager.getConnection();
				 PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, client.nom().toUpperCase());
				ps.setString(2, client.prenom());
				ps.setString(3, client.email());
				ps.setDate(4, new java.sql.Date(client.naissance().getTime()));
				ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getLong(1);
				}
			} catch (SQLException e) {
				throw new DaoException("Erreur lors de la création du client: " + e.getMessage(), e);
			}
			return 0;
		}
	}
	
	public long delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {
			ps.setLong(1, client.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression du client: " + e.getMessage(), e);
		}
	}

	public Client findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Client(id, rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getDate("naissance"));
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération du client: " + e.getMessage(), e);
		}
		return null;
	}

	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(FIND_CLIENTS_QUERY)) {
			while (rs.next()) {
				clients.add(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getDate("naissance")));
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des clients: " + e.getMessage(), e);
		}
		return clients;
	}

}
