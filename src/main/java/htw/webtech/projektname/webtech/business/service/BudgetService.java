package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Budget;
import htw.webtech.projektname.webtech.business.BudgetRepository;
import htw.webtech.projektname.webtech.business.Expense;
import htw.webtech.projektname.webtech.business.ExpenseRepository;
import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import htw.webtech.projektname.webtech.rest.model.CreateBudgetDTO;
import htw.webtech.projektname.webtech.rest.model.ExpenseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(BudgetRepository budgetRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    // Liefert nur die Budgets des übergebenen Nutzers, nie die aller Nutzer
    public List<BudgetDTO> getAllBudgets(User currentUser) {
        return budgetRepository.findByOwnerId(currentUser.getId()).stream()
                .map(b -> new BudgetDTO(b.getId(), b.getBudgetName(), b.getBudgetAmount()))
                .toList();
    }

    public BudgetDTO createBudget(CreateBudgetDTO dto, User currentUser) {
        Budget budget = new Budget(dto.budgetName(), currentUser);
        budgetRepository.save(budget);
        return new BudgetDTO(budget.getId(), budget.getBudgetName(), budget.getBudgetAmount());
    }

    public ExpenseDTO addExpense(Long budgetId, ExpenseDTO dto, User currentUser) {
        Budget budget = budgetRepository.findByIdAndOwnerId(budgetId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget mit ID " + budgetId + " nicht gefunden"));

        Expense expense = new Expense(dto.amount(), dto.date(), budget);
        expenseRepository.save(expense);

        budget.addExpenseAmount(dto.amount());
        budgetRepository.save(budget);

        return new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDate(), budget.getId());
    }

    public List<ExpenseDTO> getExpensesForBudget(Long budgetId, User currentUser) {
        // Sicherstellen, dass das Budget überhaupt dem aktuellen Nutzer gehört
        budgetRepository.findByIdAndOwnerId(budgetId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Budget mit ID " + budgetId + " nicht gefunden"));

        return expenseRepository.findByBudgetIdAndBudgetOwnerId(budgetId, currentUser.getId()).stream()
                .map(e -> new ExpenseDTO(e.getId(), e.getAmount(), e.getDate(), e.getBudget().getId()))
                .toList();
    }
}