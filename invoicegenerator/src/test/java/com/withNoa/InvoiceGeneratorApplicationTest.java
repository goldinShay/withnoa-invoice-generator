package com.withNoa;

import com.withNoa.entity.AppUser;
import com.withNoa.entity.Client;
import com.withNoa.entity.enums.Role;
import com.withNoa.repository.AppUserRepository;
import com.withNoa.repository.ClientRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class InvoiceGeneratorApplicationTest {

    @Test
    void whenRunIsCalled_thenClientAndUsersAreSeeded() throws Exception {
        // Arrange
        ClientRepository mockClientRepo = mock(ClientRepository.class);
        AppUserRepository mockUserRepo = mock(AppUserRepository.class);

        InvoiceGeneratorApplication app = new InvoiceGeneratorApplication();
        app.clientRepository = mockClientRepo;
        app.appUserRepository = mockUserRepo;

        when(mockUserRepo.findByUsername("admin")).thenReturn(Optional.empty());
        when(mockUserRepo.findByUsername("noaGoldin")).thenReturn(Optional.empty());

        // Act
        app.run();

        // Assert
        verify(mockClientRepo).save(argThat(client ->
                client.getName().equals("Test Client") &&
                        client.getEmail().equals("test@withnoa.com")
        ));

        verify(mockUserRepo).save(argThat(user ->
                user.getUsername().equals("goGoldin") &&
                        user.getPassword().equals("AmonaMakeit4show!") &&
                        user.getRole() == Role.ADMIN
        ));

        verify(mockUserRepo).save(argThat(user ->
                user.getUsername().equals("noaGoldin") &&
                        user.getPassword().equals("iloveyou!") &&
                        user.getRole() == Role.USER
        ));
    }
}
