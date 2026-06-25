package htw.webtech.projektname.webtech.rest.controller;

import htw.webtech.projektname.webtech.business.service.BudgetService;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import htw.webtech.projektname.webtech.rest.model.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {this.budgetService = budgetService;}

    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgets() {

        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    @PostMapping("/budgets")
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        return ResponseEntity.ok(budgetService.createBudget(budgetDTO));
    }

    @PostMapping("/budgets/{budgetId}/expenses")
    public ResponseEntity<ExpenseDTO> addExpense(@PathVariable Long budgetId, @RequestBody ExpenseDTO expenseDTO) {
        return ResponseEntity.ok(budgetService.addExpense(budgetId, expenseDTO));
    }

    @GetMapping("/budgets/{budgetId}/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpenses(@PathVariable Long budgetId) {
        return ResponseEntity.ok(budgetService.getExpensesForBudget(budgetId));
    }
}