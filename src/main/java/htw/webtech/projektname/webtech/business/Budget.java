package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetName;
    private int budgetAmount;

    public Budget() {}

    public Budget(String budgetName, int budgetAmount) {
        this.budgetName = budgetName;
        this.budgetAmount = 0;
    }

    public Long getId() { return id; }
    public String getBudgetName() { return budgetName; }
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    public int getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(int budgetAmount) { this.budgetAmount = budgetAmount; }

    public void addExpenseAmount(int amount) {
        this.budgetAmount += amount;
    }
}
