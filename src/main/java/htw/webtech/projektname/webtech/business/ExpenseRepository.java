package htw.webtech.projektname.webtech.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    // Wird für die Berechnung des Monatslimits genutzt - nur Ausgaben aus Budgets des jeweiligen Nutzers
    List<Expense> findByBudgetOwnerIdAndDateBetween(Long ownerId, LocalDate start, LocalDate end);

    List<Expense> findByBudgetId(Long budgetId);

    // Zusätzliche Absicherung: nur Ausgaben zurückgeben, wenn das Budget auch dem Nutzer gehört
    List<Expense> findByBudgetIdAndBudgetOwnerId(Long budgetId, Long ownerId);
}