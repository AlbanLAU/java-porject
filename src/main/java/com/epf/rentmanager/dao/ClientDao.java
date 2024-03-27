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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private ClientDao() {}

	@Autowired
	private ReservationDao reservationDao;
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String FIND_CLIENT_BY_EMAIL_QUERY = "SELECT id FROM Client WHERE email=?;";
	private static final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom=?, prenom=?, email=?, naissance=? WHERE id=?;";

	public long create(Client client) throws DaoException, ServiceException {
		LocalDate eighteenYearsAgo = LocalDate.now().minusYears(18);
		if (client.nom() == null || client.prenom() == null || client.nom().isEmpty() || client.prenom().isEmpty()) {
			throw new DaoException("Le nom et prénom du client ne peuvent pas être null.");
		} else if (client.naissance().isAfter(eighteenYearsAgo)){
			throw new DaoException("Le client doit avoir plus de 18 ans.");
		} else if (emailExists(client.email())) {
			throw new DaoException("L'email du client existe déjà.");
		} else if (client.prenom().length() < 3 || client.nom().length() < 3){
			throw new DaoException("Le nom et le prenom doivent faire au moins trois caractères.");
		} else {
			try (Connection connection = ConnectionManager.getConnection();
				 PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, client.nom().toUpperCase());
				ps.setString(2, client.prenom());
				ps.setString(3, client.email());
				ps.setDate(4, Date.valueOf(client.naissance()));
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
			reservationDao.findResaByClientId(client.id()).forEach(resa -> {
				try {
					reservationDao.delete(resa);
				} catch (DaoException e) {
					throw new RuntimeException(e);
				}
			});
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
				return new Client(id, rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getDate("naissance").toLocalDate());
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
				clients.add(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getDate("naissance").toLocalDate()));
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des clients: " + e.getMessage(), e);
		}
		return clients;
	}

	public boolean emailExists(String email) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_BY_EMAIL_QUERY)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la vérification de l'email: " + e.getMessage(), e);
		}
	}

	public void update(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENT_QUERY)) {
			ps.setString(1, client.nom());
			ps.setString(2, client.prenom());
			ps.setString(3, client.email());
			ps.setDate(4, Date.valueOf(client.naissance()));
			ps.setLong(5, client.id());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la mise à jour du client: " + e.getMessage(), e);
		}
	}

}
