package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Budget;
import htw.webtech.projektname.webtech.business.BudgetRepository;
import htw.webtech.projektname.webtech.business.Expense;
import htw.webtech.projektname.webtech.business.ExpenseRepository;
import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import htw.webtech.projektname.webtech.rest.model.CreateBudgetDTO;
import htw.webtech.projektname.webtech.rest.model.ExpenseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    private BudgetRepository budgetRepository;
    private ExpenseRepository expenseRepository;
    private BudgetService budgetService;
    private User currentUser;

    @BeforeEach
    void setUp() {
        budgetRepository = mock(BudgetRepository.class);
        expenseRepository = mock(ExpenseRepository.class);
        budgetService = new BudgetService(budgetRepository, expenseRepository);
        currentUser = new User("Aurora", "aurora", "encodedPassword");
    }

    @Test
    void createBudgetSavesBudgetForCurrentUserAndReturnsBudgetDto() {
        CreateBudgetDTO dto = new CreateBudgetDTO("Lebensmittel");

        BudgetDTO response = budgetService.createBudget(dto, currentUser);

        ArgumentCaptor<Budget> budgetCaptor = ArgumentCaptor.forClass(Budget.class);
        verify(budgetRepository).save(budgetCaptor.capture());

        Budget savedBudget = budgetCaptor.getValue();
        assertEquals("Lebensmittel", savedBudget.getBudgetName());
        assertEquals(currentUser, savedBudget.getOwner());
        assertEquals(0, savedBudget.getBudgetAmount());

        assertEquals("Lebensmittel", response.budgetName());
        assertEquals(0, response.budgetAmount());

        verifyNoInteractions(expenseRepository);
    }

    @Test
    void getAllBudgetsReturnsOnlyBudgetsOfCurrentUser() {
        Budget budget1 = new Budget("Lebensmittel", currentUser);
        Budget budget2 = new Budget("Miete", currentUser);
        when(budgetRepository.findByOwnerId(currentUser.getId()))
                .thenReturn(List.of(budget1, budget2));

        List<BudgetDTO> result = budgetService.getAllBudgets(currentUser);

        assertEquals(2, result.size());
        assertEquals("Lebensmittel", result.get(0).budgetName());
        assertEquals("Miete", result.get(1).budgetName());
        verify(budgetRepository).findByOwnerId(currentUser.getId());
    }

    @Test
    void getAllBudgetsReturnsEmptyListWhenUserHasNoBudgets() {
        when(budgetRepository.findByOwnerId(currentUser.getId()))
                .thenReturn(List.of());

        List<BudgetDTO> result = budgetService.getAllBudgets(currentUser);

        assertTrue(result.isEmpty());
    }

    @Test
    void addExpenseIncreasesBudgetAmountAndSavesExpense() {
        Budget budget = new Budget("Lebensmittel", currentUser);
        Long budgetId = 1L;
        when(budgetRepository.findByIdAndOwnerId(budgetId, currentUser.getId()))
                .thenReturn(Optional.of(budget));

        ExpenseDTO requestDto = new ExpenseDTO(null, 50, LocalDate.of(2026, 6, 1), null);

        ExpenseDTO response = budgetService.addExpense(budgetId, requestDto, currentUser);

        // Ausgabe wurde gespeichert
        ArgumentCaptor<Expense> expenseCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseCaptor.capture());
        Expense savedExpense = expenseCaptor.getValue();
        assertEquals(50, savedExpense.getAmount());
        assertEquals(LocalDate.of(2026, 6, 1), savedExpense.getDate());
        assertEquals(budget, savedExpense.getBudget());
    }
}
