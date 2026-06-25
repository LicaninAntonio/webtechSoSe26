package htw.webtech.projektname.webtech.rest.model;

import java.time.LocalDate;

public record ExpenseDTO(Long id, int amount, LocalDate date, Long budgetId) {
}