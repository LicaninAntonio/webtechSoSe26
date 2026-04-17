package htw.webtech.projektname.webtech.rest.controller;

import htw.webtech.projektname.webtech.business.service.BudgetService;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
