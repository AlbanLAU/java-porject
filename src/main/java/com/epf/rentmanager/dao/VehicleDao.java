package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {

	private VehicleDao() {}
	
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele ,nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, nb_places FROM Vehicle;";
	
	public long create(Vehicle vehicle) throws DaoException, ServiceException {
		if (vehicle.constructeur() == null ){
			throw new ServiceException("Le constructeur du véhicule ne peut pas être null.");
		} else if (vehicle.nbPlaces() < 1) {
			throw new ServiceException("Le nombre de place doit être supérieur à 1.");
		} else {
			try (Connection connection = ConnectionManager.getConnection();
				 PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, vehicle.constructeur());
				ps.setString(2, vehicle.modele());
				ps.setInt(3, vehicle.nbPlaces());
				ps.executeUpdate();
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getLong(1);
				}
			} catch (SQLException e) {
				throw new DaoException("Erreur lors de la création du véhicule: " + e.getMessage(), e);
			}
			return 0;
		}
	}

	public long delete(Vehicle vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {
			ps.setLong(1, vehicle.id());
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la suppression du véhicule: " + e.getMessage(), e);
		}
	}

	public Vehicle findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Vehicle vehicle = new Vehicle(rs.getLong("id"), rs.getString("constructeur"), rs.getString("modele"), rs.getInt("nb_places"));
					return vehicle;
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération du véhicule: " + e.getMessage(), e);
		}
		return null;
	}

	public List<Vehicle> findAll() throws DaoException {
		List<Vehicle> vehicles = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(FIND_VEHICLES_QUERY)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Vehicle vehicle = new Vehicle(rs.getLong("id"), rs.getString("constructeur"), rs.getString("modele"), rs.getInt("nb_places"));
					vehicles.add(vehicle);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Erreur lors de la récupération des véhicules: " + e.getMessage(), e);
		}
		return vehicles;
		
	}
	

}
