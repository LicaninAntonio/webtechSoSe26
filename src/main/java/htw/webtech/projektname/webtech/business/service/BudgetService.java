package htw.webtech.projektname.webtech.business.service;

import htw.webtech.projektname.webtech.rest.model.BudgetDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BudgetService {

    public List<BudgetDTO> getAllPersons() {
        return List.of(
                new BudgetDTO("household", 300),
                new BudgetDTO("leasure", 200)
        );
    }
}
