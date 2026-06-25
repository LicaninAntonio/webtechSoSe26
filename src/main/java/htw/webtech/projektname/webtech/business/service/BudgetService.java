package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Budget;
import htw.webtech.projektname.webtech.business.BudgetRepository;
import htw.webtech.projektname.webtech.business.Expense;
import htw.webtech.projektname.webtech.business.ExpenseRepository;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import htw.webtech.projektname.webtech.rest.model.CreateBudgetDTO;
import htw.webtech.projektname.webtech.rest.model.ExpenseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(BudgetRepository budgetRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<BudgetDTO> getAllBudgets() {
        return StreamSupport.stream(budgetRepository.findAll().spliterator(), false)
                .map(b -> new BudgetDTO(b.getId(), b.getBudgetName(), b.getBudgetAmount()))
                .toList();
    }

    public BudgetDTO createBudget(CreateBudgetDTO dto) {
        Budget budget = new Budget(dto.budgetName());
        budgetRepository.save(budget);
        return new BudgetDTO(budget.getId(), budget.getBudgetName(), budget.getBudgetAmount());
    }

    public ExpenseDTO addExpense(Long budgetId, ExpenseDTO dto) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("Budget mit ID " + budgetId + " nicht gefunden"));

        Expense expense = new Expense(dto.amount(), dto.date(), budget);
        expenseRepository.save(expense);

        budget.addExpenseAmount(dto.amount());
        budgetRepository.save(budget);

        return new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDate(), budget.getId());
    }

    public List<ExpenseDTO> getExpensesForBudget(Long budgetId) {
        return expenseRepository.findByBudgetId(budgetId).stream()
                .map(e -> new ExpenseDTO(e.getId(), e.getAmount(), e.getDate(), e.getBudget().getId()))
                .toList();
    }
}

