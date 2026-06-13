package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Budget;
import htw.webtech.projektname.webtech.business.BudgetRepository;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public List<BudgetDTO> getAllBudgets() {
        return StreamSupport.stream(budgetRepository.findAll().spliterator(), false)
                .map(b -> new BudgetDTO(b.getBudgetName(), b.getBudgetLimit()))
                .toList();
    }

    public BudgetDTO createBudget(BudgetDTO dto) {
        Budget budget = new Budget(dto.budgetName(), dto.budgetLimit());
        budgetRepository.save(budget);
        return dto;
    }
}