package htw.webtech.projektname.webtech.rest.controller;

import htw.webtech.projektname.webtech.business.service.MonthlyLimitService;
import htw.webtech.projektname.webtech.rest.model.MonthlyLimitDTO;
import htw.webtech.projektname.webtech.rest.model.SetMonthlyLimitDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/monthly-limits")
public class MonthlyLimitController {

    private final MonthlyLimitService monthlyLimitService;

    public MonthlyLimitController(MonthlyLimitService monthlyLimitService) {
        this.monthlyLimitService = monthlyLimitService;
    }

    // Liefert das Limit für einen Monat (Format "YYYY-MM") inkl. verfügbarem Restbetrag.
    @GetMapping("/{month}")
    public ResponseEntity<MonthlyLimitDTO> getLimitForMonth(@PathVariable String month) {
        return ResponseEntity.ok(monthlyLimitService.getLimitForMonth(month));
    }

    // Setzt das Limit für einen Monat neu, oder legt es an falls noch nicht vorhanden.
    // Wird auch genutzt, um ein bestehendes Limit während des Monats anzupassen.
    @PutMapping("/{month}")
    public ResponseEntity<MonthlyLimitDTO> setLimitForMonth(@PathVariable String month, @RequestBody SetMonthlyLimitDTO dto) {
        return ResponseEntity.ok(monthlyLimitService.setLimit(month, dto.limitAmount()));
    }
}