package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

// Ein Monatslimit ist immer nur für eine Kombination aus Nutzer + Monat eindeutig,
// nicht global - sonst würden sich alle Nutzer ein einziges Limit pro Monat teilen.
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"month", "user_id"}))
public class MonthlyLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Format: "YYYY-MM", z.B. "2026-06" -> entspricht dem Wert von <input type="month">
    private String month;

    private int limitAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User owner;

    public MonthlyLimit() {}

    public MonthlyLimit(String month, int limitAmount, User owner) {
        this.month = month;
        this.limitAmount = limitAmount;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public int getLimitAmount() { return limitAmount; }
    public void setLimitAmount(int limitAmount) { this.limitAmount = limitAmount; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}