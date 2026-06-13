package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetName;
    private int budgetLimit;

    public Budget() {}

    public Budget(String budgetName, int budgetLimit) {
        this.budgetName = budgetName;
        this.budgetLimit = budgetLimit;
    }

    public Long getId() { return id; }
    public String getBudgetName() { return budgetName; }
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    public int getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(int budgetLimit) { this.budgetLimit = budgetLimit; }
}
