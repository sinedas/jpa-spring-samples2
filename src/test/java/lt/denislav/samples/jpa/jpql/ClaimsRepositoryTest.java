package lt.denislav.samples.jpa.jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lt.denislav.samples.jpa.BaseTest;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.BaseEntity;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.domain.Person;
import lt.denislav.samples.jpa.domain.PersonDetail;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestEntityManager
public class ClaimsRepositoryTest extends BaseTest {

    @Autowired
    private ClaimsRepository claimsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void prepareClaims() {
        for (int i = 0; i < 10; i++) {
            Claim claim = new Claim();
            claim.setStatus(ClaimStatus.OPEN);
            claim.getPersons()
                    .add(new Person("John"));
            claim.getPersons()
                    .add(new Person("Peter"));
            claimsRepository.save(claim);
        }

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.OPEN);
        claim.getPersons()
                .add(new Person());
        claimsRepository.save(claim);
        entityManager.flush();
        entityManager.clear();

        logDashes("Before");
    }

    @After
    public void deleteClaims() {
        logDashes("After");

        claimsRepository.deleteAll();
        entityManager.flush();

    }

    @Transactional(readOnly = true)
    @Test
    public void findAll() {
        List<Claim> list = claimsRepository.findAll();

        log.info("Found claims: " + list);
        log.info("Found claims: " + list.get(0).getPersons());

        assertThat(list.size(), equalTo(3));
    }

    @Transactional(readOnly = true)
    @Test
    public void findByStatus() {
        List<Claim> list = claimsRepository.findByStatus(ClaimStatus.OPEN);

        log.info("Found claims: " + list);
        log.info("Found claims: " + list.get(0).getPersons());

        assertThat(list.size(), equalTo(3));
    }

    @Transactional(readOnly = true)
    @Test
    public void findClaims() {
        List<Claim> list = claimsRepository.findClaims();

        log.info("Found claims: " + list);
        log.info("Found claims: " + list.get(0).getPersons());

        assertThat(list.size(), equalTo(3));
    }

    @Transactional(readOnly = true)
    @Test
    public void findClaimsWithFetchJoin() {
        List<Claim> list = claimsRepository.findClaims2();

        log.info("Found claims: " + list);
        log.info("Found claims: " + list.get(0).getPersons());

        assertThat(list.size(), equalTo(3));
    }

    @Transactional(readOnly = true)
    @Test
    public void countClaims() {
        Long count = claimsRepository.countClaims(ClaimStatus.OPEN);

        assertThat(count, equalTo(3L));
    }

    @Transactional
    @Test
    public void updateClaims() {
        claimsRepository.updateClaimStatus(ClaimStatus.CLOSED);

        Long count = claimsRepository.countClaims(ClaimStatus.CLOSED);

        assertThat(count, equalTo(3L));
    }

    @Transactional
    //@Test
    public void deleteClaimsByStatus() {
        claimsRepository.deleteClaimsByStatus(ClaimStatus.OPEN);

        Long count = claimsRepository.countClaims(ClaimStatus.CLOSED);

        assertThat(count, equalTo(3L));
    }

    @Transactional(readOnly = true)
    @Test
    public void findClaimsByPerson() {
        Page<Claim> claims = claimsRepository.findClaimsByPerson(PageRequest.of(0, 5), "John");

        System.err.println(claims.getTotalElements());
        claims.getContent().stream()
                .forEach(claim -> {
                    System.err.println(claim);
                    System.err.println(claim.getPersons());
                });
    }
}
