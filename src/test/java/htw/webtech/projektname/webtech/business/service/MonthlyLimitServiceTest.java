package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Expense;
import htw.webtech.projektname.webtech.business.ExpenseRepository;
import htw.webtech.projektname.webtech.business.MonthlyLimit;
import htw.webtech.projektname.webtech.business.MonthlyLimitRepository;
import htw.webtech.projektname.webtech.business.User;
import htw.webtech.projektname.webtech.rest.model.MonthlyLimitDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonthlyLimitServiceTest {

    private MonthlyLimitRepository monthlyLimitRepository;
    private ExpenseRepository expenseRepository;
    private MonthlyLimitService monthlyLimitService;
    private User currentUser;

    @BeforeEach
    void setUp() {
        monthlyLimitRepository = mock(MonthlyLimitRepository.class);
        expenseRepository = mock(ExpenseRepository.class);
        monthlyLimitService = new MonthlyLimitService(monthlyLimitRepository, expenseRepository);
        currentUser = new User("Aurora", "aurora", "encodedPassword");
    }

    private Expense expenseWithAmount(int amount) {
        Expense expense = mock(Expense.class);
        when(expense.getAmount()).thenReturn(amount);
        return expense;
    }

    @Test
    void getLimitForMonthCalculatesAvailableAmountCorrectly() {
        MonthlyLimit monthlyLimit = new MonthlyLimit("2026-07", 500, currentUser);

        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.of(monthlyLimit));

        List<Expense> expenses = List.of(expenseWithAmount(120), expenseWithAmount(80));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(
                currentUser.getId(),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31)
        )).thenReturn(expenses);

        MonthlyLimitDTO result = monthlyLimitService.getLimitForMonth("2026-07", currentUser);

        assertEquals("2026-07", result.month());
        assertEquals(500, result.limitAmount());
        assertEquals(300, result.availableAmount());

        verify(monthlyLimitRepository).findByMonthAndOwnerId("2026-07", currentUser.getId());
        verify(expenseRepository).findByBudgetOwnerIdAndDateBetween(
                currentUser.getId(),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31)
        );
    }

    @Test
    void getLimitForMonthReturnsZeroDefaultsWhenNoLimitSetForUser() {
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-08", currentUser.getId()))
                .thenReturn(Optional.empty());

        MonthlyLimitDTO result = monthlyLimitService.getLimitForMonth("2026-08", currentUser);

        assertNull(result.id());
        assertEquals("2026-08", result.month());
        assertEquals(0, result.limitAmount());
        assertEquals(0, result.availableAmount());

        // Ohne existierendes Limit darf gar nicht erst nach Ausgaben gefragt werden
        verifyNoInteractions(expenseRepository);
    }

    @Test
    void getLimitForMonthReturnsFullLimitWhenNoExpensesYet() {
        MonthlyLimit monthlyLimit = new MonthlyLimit("2026-07", 500, currentUser);
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.of(monthlyLimit));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(List.of());

        MonthlyLimitDTO result = monthlyLimitService.getLimitForMonth("2026-07", currentUser);

        assertEquals(500, result.availableAmount());
    }

    @Test
    void getLimitForMonthAllowsNegativeAvailableAmountWhenOverspent() {
        MonthlyLimit monthlyLimit = new MonthlyLimit("2026-07", 100, currentUser);
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.of(monthlyLimit));
        List<Expense> expenses = List.of(expenseWithAmount(150));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(expenses);

        MonthlyLimitDTO result = monthlyLimitService.getLimitForMonth("2026-07", currentUser);

        // Überziehen wird als negativer Betrag angezeigt
        assertEquals(-50, result.availableAmount());
    }

    @Test
    void getLimitForMonthUsesCorrectDateRangeForShorterMonth() {
        MonthlyLimit monthlyLimit = new MonthlyLimit("2026-02", 200, currentUser);
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-02", currentUser.getId()))
                .thenReturn(Optional.of(monthlyLimit));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(List.of());

        monthlyLimitService.getLimitForMonth("2026-02", currentUser);

        // Februar 2026 hat 28 Tage (kein Schaltjahr) - stellt sicher, dass YearMonth.atEndOfMonth()
        // korrekt verwendet wird und nicht z.B. hart auf Tag 31 gerechnet wird
        verify(expenseRepository).findByBudgetOwnerIdAndDateBetween(
                currentUser.getId(),
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28)
        );
    }

    @Test
    void setLimitCreatesNewMonthlyLimitWhenNoneExistsYet() {
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.empty());
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(List.of());

        MonthlyLimitDTO result = monthlyLimitService.setLimit("2026-07", 400, currentUser);

        ArgumentCaptor<MonthlyLimit> captor = ArgumentCaptor.forClass(MonthlyLimit.class);
        verify(monthlyLimitRepository).save(captor.capture());

        MonthlyLimit saved = captor.getValue();
        assertEquals("2026-07", saved.getMonth());
        assertEquals(400, saved.getLimitAmount());
        assertEquals(currentUser, saved.getOwner());

        assertEquals("2026-07", result.month());
        assertEquals(400, result.limitAmount());
        assertEquals(400, result.availableAmount());
    }

    @Test
    void setLimitUpdatesExistingMonthlyLimitInsteadOfCreatingDuplicate() {
        MonthlyLimit existingLimit = new MonthlyLimit("2026-07", 300, currentUser);
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.of(existingLimit));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(List.of());

        MonthlyLimitDTO result = monthlyLimitService.setLimit("2026-07", 450, currentUser);

        ArgumentCaptor<MonthlyLimit> captor = ArgumentCaptor.forClass(MonthlyLimit.class);
        verify(monthlyLimitRepository).save(captor.capture());

        // Es wird das bestehende Objekt aktualisiert, kein neues angelegt
        assertSame(existingLimit, captor.getValue());
        assertEquals(450, existingLimit.getLimitAmount());
        assertEquals(450, result.limitAmount());

        // Es darf nur genau einmal gespeichert werden
        verify(monthlyLimitRepository, times(1)).save(any());
    }

    @Test
    void setLimitReflectsAlreadySpentAmountInAvailableAmount() {
        when(monthlyLimitRepository.findByMonthAndOwnerId("2026-07", currentUser.getId()))
                .thenReturn(Optional.empty());
        List<Expense> expenses = List.of(expenseWithAmount(100));
        when(expenseRepository.findByBudgetOwnerIdAndDateBetween(any(), any(), any()))
                .thenReturn(expenses);

        MonthlyLimitDTO result = monthlyLimitService.setLimit("2026-07", 400, currentUser);

        assertEquals(400, result.limitAmount());
        assertEquals(300, result.availableAmount());
    }
}