package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
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
	private static final String FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_QUERY = "SELECT id FROM Reservation WHERE vehicle_id=? AND debut<=? AND fin>=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_RANGE_QUERY = "SELECT id FROM Reservation WHERE vehicle_id=? AND ((debut BETWEEN ? AND ?) OR (fin BETWEEN ? AND ?));";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, debut=?, fin=? WHERE id=?;";

	public long create(Reservation reservation) throws DaoException {
		long daysBetween = ChronoUnit.DAYS.between(reservation.debut(), reservation.fin());
		if (reservation.debut().isAfter(reservation.fin())) {
			throw new DaoException("La date de début doit être avant la date de fin.");
		} else if (daysBetween > 7) {
			throw new DaoException("L'écart entre la date de début et de fin ne peut pas être de plus de 7 jours.");
		} else if (reservationExistsForVehicleAndDate(reservation.vehicleId(), reservation.debut()) ||
				reservationExistsForVehicleAndDate(reservation.vehicleId(), reservation.fin())) {
			throw new DaoException("La voiture est déjà réservée pour cette date.");
		} else if (reservationExistsForVehicleAndDateRange(reservation.vehicleId(), reservation.debut(), reservation.fin())) {
			throw new DaoException("La voiture ne peut pas être réservée 30 jours de suite sans pause.");
		} else {
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

	public boolean reservationExistsForVehicleAndDate(long vehicleId, LocalDate date) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_QUERY)) {
			ps.setLong(1, vehicleId);
			ps.setDate(2, Date.valueOf(date));
			ps.setDate(3, Date.valueOf(date));
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la vérification de la réservation: " + e.getMessage(), e);
		}
	}

	public boolean reservationExistsForVehicleAndDateRange(long vehicleId, LocalDate startDate, LocalDate endDate) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_AND_DATE_RANGE_QUERY)) {
			ps.setLong(1, vehicleId);
			ps.setDate(2, Date.valueOf(startDate));
			ps.setDate(3, Date.valueOf(endDate));
			ps.setDate(4, Date.valueOf(startDate));
			ps.setDate(5, Date.valueOf(endDate));
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la vérification de la réservation: " + e.getMessage(), e);
		}
	}

	public void update(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATION_QUERY)) {
			ps.setLong(1, reservation.clientId());
			ps.setLong(2, reservation.vehicleId());
			ps.setDate(3, Date.valueOf(reservation.debut()));
			ps.setDate(4, Date.valueOf(reservation.fin()));
			ps.setLong(5, reservation.id());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la mise à jour de la réservation: " + e.getMessage(), e);
		}
	}
}
