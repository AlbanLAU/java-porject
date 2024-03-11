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

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

	private ReservationDao() {}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
		
	public long create(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setLong(1, reservation.clientId());
			ps.setLong(2, reservation.vehicleId());
			ps.setDate(3, Date.valueOf(reservation.debut()));
			ps.setDate(4, Date.valueOf(reservation.fin()));
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la création de la réservation: " + e.getMessage(), e);
		}
		return 0;
	}
	
	public long delete(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {
			ps.setLong(1, reservation.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression de la réservation: " + e.getMessage(), e);
		}
	}

	
	public List<Reservation> findResaByClientId(long clientId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {
			ps.setLong(1, clientId);
			ResultSet rs = ps.executeQuery();
			List<Reservation> reservations = new ArrayList<>();
			while (rs.next()) {

				Reservation reservation = new Reservation(	rs.getLong("id"),
															clientId,
															rs.getLong("vehicle_id"),
															rs.getDate("debut").toLocalDate(),
															rs.getDate("fin").toLocalDate()
				);
				reservations.add(reservation);
			}
			return reservations;
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des réservations: " + e.getMessage(), e);
		}
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {
			ps.setLong(1, vehicleId);
			ResultSet rs = ps.executeQuery();
			List<Reservation> reservations = new ArrayList<>();
			while (rs.next()) {
				Reservation reservation = new Reservation( rs.getLong("id"),
															rs.getLong("client_id"),
															vehicleId,
															rs.getDate("debut").toLocalDate(),
															rs.getDate("fin").toLocalDate()
				);
				reservations.add(reservation);
			}
			return reservations;
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des réservations: " + e.getMessage(), e);
		}
	}

	public List<Reservation> findAll() throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(FIND_RESERVATIONS_QUERY)) {
			while (rs.next()) {
				Reservation reservation = new Reservation(
						rs.getLong("id"),
						rs.getLong("client_id"),
						rs.getLong("vehicle_id"),
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()
				);
				reservations.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des réservations: " + e.getMessage(), e);
		}
		return reservations;
	}

	public Reservation findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement("SELECT * FROM Reservation WHERE id=?")) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Reservation reservation = new Reservation(
						rs.getLong("id"),
						rs.getLong("client_id"),
						rs.getLong("vehicle_id"),
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()
				);
				return reservation;
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération de la réservation: " + e.getMessage(), e);
		}
		return null;
	}
}
