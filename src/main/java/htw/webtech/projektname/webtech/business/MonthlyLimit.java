package htw.webtech.projektname.webtech.business;

import jakarta.persistence.*;

@Entity
public class MonthlyLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Format: "YYYY-MM", z.B. "2026-06" -> entspricht dem Wert von <input type="month">
    @Column(unique = true)
    private String month;

    private int limitAmount;

    public MonthlyLimit() {}

    public MonthlyLimit(String month, int limitAmount) {
        this.month = month;
        this.limitAmount = limitAmount;
    }

    public Long getId() { return id; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public int getLimitAmount() { return limitAmount; }
    public void setLimitAmount(int limitAmount) { this.limitAmount = limitAmount; }
}