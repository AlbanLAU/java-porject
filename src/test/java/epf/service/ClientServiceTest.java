package epf.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
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
public class ClientServiceTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void create_client_with_valid_data() throws ServiceException, DaoException {
        Client client = new Client(1L, "Bob", "Marley", "bob.marley@epfedu.com", LocalDate.of(2002, 12, 12));
        when(clientDao.create(any(Client.class))).thenReturn(1L);

        long result = clientDao.create(client);

        assertEquals(1L, result);
        verify(clientDao, times(1)).create(client);
    }

    @Test
    public void find_client_by_id() throws ServiceException, DaoException {
        Client client = new Client(1L, "Bob", "Marley", "bob.marley@epfedu.com", LocalDate.of(2002, 12, 12));
        when(clientDao.findById(1L)).thenReturn(client);

        Client result = clientDao.findById(1L);

        assertEquals(client, result);
        verify(clientDao, times(1)).findById(1L);
    }

    @Test
    public void find_all_clients() throws ServiceException, DaoException {
        Client client = new Client(1L, "Bob", "Marley", "bob.marley@epfedu.com", LocalDate.of(2002, 12, 12));
        when(clientDao.findAll()).thenReturn(List.of(client));

        List<Client> result = clientDao.findAll();

        assertEquals(List.of(client), result);
        verify(clientDao, times(1)).findAll();
    }

    @Test
    public void delete_client() throws ServiceException, DaoException {
        Client client = new Client(1L, "Bob", "Marley", "bob.marley@epfedu.com", LocalDate.of(2002, 12, 12));
        when(clientDao.delete(client)).thenReturn(1L);

        long result = clientDao.delete(client);
        assertEquals(1L, result);
        verify(clientDao, times(1)).delete(client);
    }
}
