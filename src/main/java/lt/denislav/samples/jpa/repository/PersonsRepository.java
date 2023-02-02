package lt.denislav.samples.jpa.repository;

import java.util.List;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PersonsRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p where id = :person")
    Person findPersonById(@Param("person") Long id);
}
