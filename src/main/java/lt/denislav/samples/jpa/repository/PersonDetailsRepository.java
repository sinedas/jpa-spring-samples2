package lt.denislav.samples.jpa.repository;

import lt.denislav.samples.jpa.domain.PersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PersonDetailsRepository extends JpaRepository<PersonDetail, Long> {
}
