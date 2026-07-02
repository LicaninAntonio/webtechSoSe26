package htw.webtech.projektname.webtech.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Sorgt dafür, dass nicht authentifizierte Requests eine saubere 401-JSON-Antwort bekommen
// statt eines HTML-Login-Formulars (Standardverhalten von Spring Security).
// Das JSON wird hier bewusst von Hand gebaut (kein ObjectMapper nötig) - das Feld ist statisch
// und enthält keine Nutzereingaben, daher ist kein Escaping notwendig.
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"Nicht angemeldet oder Sitzung abgelaufen.\"}");
    }
}