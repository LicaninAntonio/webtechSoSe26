package htw.webtech.projektname.webtech.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    List<Expense> findByBudgetId(Long budgetId);
}