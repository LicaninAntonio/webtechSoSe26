package htw.webtech.projektname.webtech.business;

import htw.webtech.projektname.webtech.business.Budget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {}
