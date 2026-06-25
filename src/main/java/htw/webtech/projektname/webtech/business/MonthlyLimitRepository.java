package htw.webtech.projektname.webtech.business;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyLimitRepository extends CrudRepository<MonthlyLimit, Long> {

    Optional<MonthlyLimit> findByMonth(String month);
}