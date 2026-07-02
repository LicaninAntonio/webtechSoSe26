package htw.webtech.projektname.webtech.rest.model;

// Enthält bewusst nie das Passwort - wird an das Frontend zurückgegeben
public record UserDTO(Long id, String name, String username) {
}