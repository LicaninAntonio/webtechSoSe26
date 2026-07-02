package htw.webtech.projektname.webtech.rest.controller;

import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.business.service.BudgetService;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import htw.webtech.projektname.webtech.rest.model.CreateBudgetDTO;
import htw.webtech.projektname.webtech.rest.model.ExpenseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {this.budgetService = budgetService;}

    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgets(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.getAllBudgets(currentUser));
    }

    @PostMapping("/budgets")
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody CreateBudgetDTO createBudgetDTO,
                                                  @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.createBudget(createBudgetDTO, currentUser));
    }

    @PostMapping("/budgets/{budgetId}/expenses")
    public ResponseEntity<ExpenseDTO> addExpense(@PathVariable Long budgetId,
                                                 @RequestBody ExpenseDTO expenseDTO,
                                                 @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.addExpense(budgetId, expenseDTO, currentUser));
    }

    @GetMapping("/budgets/{budgetId}/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpenses(@PathVariable Long budgetId,
                                                        @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(budgetService.getExpensesForBudget(budgetId, currentUser));
    }
}