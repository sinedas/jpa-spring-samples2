package lt.denislav.samples.jpa.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.QueryHints.HINT_CACHEABLE;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

public interface ClaimsRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByStatus(ClaimStatus claimStatus);

    @Query(value = "SELECT * FROM Claim c WHERE c.status = :claimStatus",
            nativeQuery = true)
    List<Claim> findByStatusNative(@Param("claimStatus") String claimStatus);


    @Query("select c from Claim c")
    List<Claim> findClaims();

    @Query("select distinct c from Claim c left join fetch c.persons p ")
    List<Claim> findClaims2();

    @Query("select count(c) from Claim c where c.status = :claimStatus")
    Long countClaims(@Param("claimStatus") ClaimStatus claimStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Claim c SET c.status = :claimStatus")
    void updateClaimStatus(@Param("claimStatus") ClaimStatus claimStatus);

    @Modifying
    @Transactional
    @Query("DELETE from Claim c WHERE c.status = :claimStatus")
    void deleteClaimsByStatus(@Param("claimStatus") ClaimStatus claimStatus);

    @Query("select c from Claim c left join c.persons p where p.name is not null and p.name in (:name)")
    Page<Claim> findClaimsByPerson(Pageable pageable, @Param("name") String name );

    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "" + 3),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = READ_ONLY, value = "true")
    })
    @Query("select c from Claim c")
    Stream<Claim> getAll();
}
