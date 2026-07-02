package htw.webtech.projektname.webtech.business;

import htw.webtech.projektname.webtech.business.Budget;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {

    List<Budget> findByOwnerId(Long ownerId);

    // Wird genutzt, um sicherzustellen, dass ein Nutzer nur auf seine eigenen Budgets zugreift
    Optional<Budget> findByIdAndOwnerId(Long id, Long ownerId);
}