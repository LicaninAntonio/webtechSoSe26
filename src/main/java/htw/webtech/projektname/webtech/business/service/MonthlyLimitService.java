package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.business.Expense;
import htw.webtech.projektname.webtech.business.ExpenseRepository;
import htw.webtech.projektname.webtech.business.MonthlyLimit;
import htw.webtech.projektname.webtech.business.MonthlyLimitRepository;
import htw.webtech.projektname.webtech.rest.model.MonthlyLimitDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class MonthlyLimitService {

    private final MonthlyLimitRepository monthlyLimitRepository;
    private final ExpenseRepository expenseRepository;

    public MonthlyLimitService(MonthlyLimitRepository monthlyLimitRepository, ExpenseRepository expenseRepository) {
        this.monthlyLimitRepository = monthlyLimitRepository;
        this.expenseRepository = expenseRepository;
    }

    // Setzt das Limit für einen Monat neu, oder legt es an, falls es noch keins gibt.
    // Erlaubt auch das Anpassen während des laufenden Monats (Upsert-Logik).
    public MonthlyLimitDTO setLimit(String month, int limitAmount) {
        MonthlyLimit monthlyLimit = monthlyLimitRepository.findByMonth(month)
                .orElse(new MonthlyLimit(month, 0));

        monthlyLimit.setMonth(month);
        monthlyLimit.setLimitAmount(limitAmount);
        monthlyLimitRepository.save(monthlyLimit);

        return toDto(monthlyLimit);
    }

    // Liefert das Limit für einen Monat inkl. berechnetem verfügbarem Restbetrag.
    // Falls für den Monat noch kein Limit gesetzt wurde, wird limitAmount/availableAmount = 0 zurückgegeben.
    public MonthlyLimitDTO getLimitForMonth(String month) {
        return monthlyLimitRepository.findByMonth(month)
                .map(this::toDto)
                .orElse(new MonthlyLimitDTO(null, month, 0, 0));
    }

    private MonthlyLimitDTO toDto(MonthlyLimit monthlyLimit) {
        int spent = getSpentAmountForMonth(monthlyLimit.getMonth());
        int available = monthlyLimit.getLimitAmount() - spent;
        return new MonthlyLimitDTO(monthlyLimit.getId(), monthlyLimit.getMonth(), monthlyLimit.getLimitAmount(), available);
    }

    private int getSpentAmountForMonth(String month) {
        YearMonth yearMonth = YearMonth.parse(month); // erwartet Format "YYYY-MM"
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Expense> expenses = expenseRepository.findByDateBetween(start, end);
        return expenses.stream().mapToInt(Expense::getAmount).sum();
    }
}
