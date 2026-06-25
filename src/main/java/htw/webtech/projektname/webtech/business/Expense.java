package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    public Expense() {}

    public Expense(int amount, LocalDate date, Budget budget) {
        this.amount = amount;
        this.date = date;
        this.budget = budget;
    }

    public Long getId() { return id; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }
}
