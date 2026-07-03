package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.business.UserRepository;
import htw.webtech.projektname.webtech.rest.model.AuthResponseDTO;
import htw.webtech.projektname.webtech.rest.model.RegisterDTO;
import htw.webtech.projektname.webtech.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void registerCreatesUserWithEncodedPasswordAndReturnsToken() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);

        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

        RegisterDTO dto = new RegisterDTO("Aurora", "aurora", "geheim123");

        when(userRepository.existsByUsername("aurora")).thenReturn(false);
        when(passwordEncoder.encode("geheim123")).thenReturn("encodedPassword");
        when(jwtService.generateToken("aurora")).thenReturn("test-token");

        AuthResponseDTO response = authService.register(dto);

        verify(userRepository).existsByUsername("aurora");
        verify(passwordEncoder).encode("geheim123");
        verify(jwtService).generateToken("aurora");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Aurora", savedUser.getName());
        assertEquals("aurora", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());

        assertEquals("test-token", response.token());
        assertEquals("Aurora", response.user().name());
        assertEquals("aurora", response.user().username());
    }

    @Test
    void registerThrowsWhenUsernameAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);
        AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);

        when(userRepository.existsByUsername("aurora")).thenReturn(true);

        RegisterDTO dto = new RegisterDTO("Aurora", "aurora", "geheim123");

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerThrowsWhenPasswordTooShort() {
        AuthService authService = new AuthService(mock(UserRepository.class), mock(PasswordEncoder.class), mock(JwtService.class));
        RegisterDTO dto = new RegisterDTO("Aurora", "aurora", "123");

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }

    @Test
    void registerThrowsWhenFieldsAreBlank() {
        AuthService authService = new AuthService(mock(UserRepository.class), mock(PasswordEncoder.class), mock(JwtService.class));
        RegisterDTO dto = new RegisterDTO("", "aurora", "geheim123");

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }
}