package lt.denislav.samples.jpa;

import java.util.Date;
import java.util.List;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityLifeCycle2Test extends BaseTest {

    private Long claimId;

    @Autowired
    private ClaimsRepository claimsRepository;


    /**
     * {@link Claim} is persisted.
     */
    @Before
    @Transactional
    public void prepareClaim() {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.NOTIFICATION);
        claim.setDate(new Date());

        claim = claimsRepository.saveAndFlush(claim);

        claimId = claim.getId();

        log.info("claim id: " + claimId);
        logDashes("before");
    }

    @After
    public void deleteClaim() {
        logDashes("after");

        claimsRepository.deleteAll();
    }

    @Test
    @Transactional
    public void noflush() {
        List<Claim> claims = claimsRepository.findByStatus(ClaimStatus.NOTIFICATION);
        assertThat(claims.size(), equalTo(1));
    }


    @Test
    @Transactional
    public void autoflush() {
        Claim claim = claimsRepository.findAll().get(0);
        claim.setStatus(ClaimStatus.CLOSED);

        List<Claim> claims = claimsRepository.findByStatus(ClaimStatus.NOTIFICATION);
        assertThat(claims.size(), equalTo(0));
    }

    @Test
    @Transactional
    public void autoflushNative() {
        Claim claim = claimsRepository.findAll().get(0);
        claim.setStatus(ClaimStatus.CLOSED);

        List<Claim> claims = claimsRepository.findByStatusNative(ClaimStatus.NOTIFICATION.toString());
        assertThat(claims.size(), equalTo(0));
    }


}
