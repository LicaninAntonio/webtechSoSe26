package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.business.UserRepository;
import htw.webtech.projektname.webtech.rest.model.AuthResponseDTO;
import htw.webtech.projektname.webtech.rest.model.LoginDTO;
import htw.webtech.projektname.webtech.rest.model.RegisterDTO;
import htw.webtech.projektname.webtech.rest.model.UserDTO;
import htw.webtech.projektname.webtech.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO register(RegisterDTO dto) {
        if (isBlank(dto.name()) || isBlank(dto.username()) || isBlank(dto.password())) {
            throw new IllegalArgumentException("Name, Benutzername und Passwort sind erforderlich.");
        }

        if (dto.password().length() < 6) {
            throw new IllegalArgumentException("Das Passwort muss mindestens 6 Zeichen lang sein.");
        }

        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Dieser Benutzername ist bereits vergeben.");
        }

        User user = new User(dto.name().trim(), dto.username().trim(), passwordEncoder.encode(dto.password()));
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponseDTO login(LoginDTO dto) {
        if (isBlank(dto.username()) || isBlank(dto.password())) {
            throw new IllegalArgumentException("Benutzername und Passwort sind erforderlich.");
        }

        User user = userRepository.findByUsername(dto.username().trim())
                .orElseThrow(() -> new BadCredentialsException("Benutzername oder Passwort ist falsch."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Benutzername oder Passwort ist falsch.");
        }

        return buildAuthResponse(user);
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getUsername());
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getUsername());
        return new AuthResponseDTO(token, userDTO);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}