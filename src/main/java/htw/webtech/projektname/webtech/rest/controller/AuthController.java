package htw.webtech.projektname.webtech.rest.controller;

import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.business.service.AuthService;
import htw.webtech.projektname.webtech.rest.model.AuthResponseDTO;
import htw.webtech.projektname.webtech.rest.model.LoginDTO;
import htw.webtech.projektname.webtech.rest.model.RegisterDTO;
import htw.webtech.projektname.webtech.rest.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // Liefert die Daten des aktuell angemeldeten Nutzers (z.B. für einen Reload im Frontend)
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserDTO(user.getId(), user.getName(), user.getUsername()));
    }
}