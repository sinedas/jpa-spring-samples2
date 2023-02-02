package lt.denislav.samples.jpa.repository;

import lt.denislav.samples.jpa.domain.Policy;
import org.springframework.data.repository.CrudRepository;

public interface PolicyRepository extends CrudRepository<Policy, Long> {
}
