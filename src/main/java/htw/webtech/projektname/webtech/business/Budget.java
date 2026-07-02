package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetName;
    private int budgetAmount;

    // Jedes Budget gehört genau einem Nutzer - so sieht jeder nur seine eigenen Budgets
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User owner;

    public Budget() {}

    public Budget(String budgetName, User owner) {
        this.budgetName = budgetName;
        this.budgetAmount = 0;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getBudgetName() { return budgetName; }
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    public int getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(int budgetAmount) { this.budgetAmount = budgetAmount; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public void addExpenseAmount(int amount) {
        this.budgetAmount += amount;
    }
}