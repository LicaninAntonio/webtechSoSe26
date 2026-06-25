package htw.webtech.projektname.webtech.rest.model;

public record MonthlyLimitDTO(Long id, String month, int limitAmount, int availableAmount) {
}