package epf.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ReservationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class RentServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void create_rent_with_valid_data() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.create(any(Reservation.class))).thenReturn(1L);

        long result = reservationDao.create(reservation);

        assertEquals(1L, result);
        verify(reservationDao, times(1)).create(reservation);
    }

    @Test
    public void find_rent_by_id() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.findById(1L)).thenReturn(reservation);

        Reservation result = reservationDao.findById(1L);

        assertEquals(reservation, result);
        verify(reservationDao, times(1)).findById(1L);
    }

    @Test
    public void find_all_rents() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.findAll()).thenReturn(List.of(reservation));

        List<Reservation> result = reservationDao.findAll();

        assertEquals(List.of(reservation), result);
        verify(reservationDao, times(1)).findAll();
    }

    @Test
    public void delete_rent() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.delete(reservation)).thenReturn(1L);

        Long result = reservationDao.delete(reservation);

        assertEquals(result, 1L);
        verify(reservationDao, times(1)).delete(reservation);
    }

    @Test
    public void count_rent() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.findAll()).thenReturn(List.of(reservation));

        Number result = reservationService.count();

        assertEquals(result, 1);
        verify(reservationDao, times(1)).findAll();
    }

    @Test
    public void find_rent_by_client() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.findResaByClientId(1L)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationDao.findResaByClientId(1L);

        assertEquals(List.of(reservation), result);
        verify(reservationDao, times(1)).findResaByClientId(1L);
    }

    @Test
    public void find_rent_by_vehicle() throws ServiceException, DaoException {
        Reservation reservation = new Reservation(1L, 1L, 1L, LocalDate.of(2021, 12, 12), LocalDate.of(2021, 12, 15));
        when(reservationDao.findResaByVehicleId(1L)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationDao.findResaByVehicleId(1L);

        assertEquals(List.of(reservation), result);
        verify(reservationDao, times(1)).findResaByVehicleId(1L);
    }
}
