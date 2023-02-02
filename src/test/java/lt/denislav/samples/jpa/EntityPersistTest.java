package lt.denislav.samples.jpa;

import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.BaseEntity;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityPersistTest extends BaseTest {

    @Autowired
    ClaimsRepository claimsRepository;

    @Autowired
    EntityManager entityManager;

    /**
     * Test saves and loads {@link Claim} entity.
     * <p>
     * You can play with {@link GeneratedValue} annotations in {@link BaseEntity} to see
     * different id generation strategies.
     */
    //@Before
    @Test
    @Transactional
    public void saveClaim() {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.OPEN);
        claim.setDate(new Date());
        claim = claimsRepository.save(claim);

        log.debug("Saved claim:" + claim);
        entityManager.flush();
        entityManager.clear();

        claim = claimsRepository.findById(claim.getId()).get();

        //LOG.debug("Loaded claim:" +  claim);
    }

    /**
     * Tests saving 10 {@link Claim} entities.
     *
     * <p>
     * Uncomment {@SequenceGenerator} annotations in {@link BaseEntity}
     * and can play with allocationSize property.
     * </p>
     */
    @Test
    @Transactional
    public void saveTenClaims() {
        for (int i = 0; i < 10; i++) {
            Claim claim = claimsRepository.save(new Claim());

            log.debug("claim saved, claim id: " + claim.getId());
        }
    }
}
