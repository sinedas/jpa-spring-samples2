package lt.denislav.samples.jpa;

import java.util.Date;
import javax.persistence.GeneratedValue;
import lombok.extern.slf4j.Slf4j;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.BaseEntity;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityPersistTest extends BaseTest{

    @Autowired
    ClaimsDao claimsDao;

    /**
     * Test saves and loads {@link Claim} entity.
     *
     * You can play with {@link GeneratedValue} annotations in {@link BaseEntity} to see
     * different id generation strategies.
     */
    //@Before
    public void saveClaim() {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.OPEN);
        claim.setDate(new Date());
        claim = claimsDao.save(claim);

        log.debug("Saved claim:" + claim);

        claim = claimsDao.findById(claim.getId());

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
        for (int i = 0; i < 10; i ++) {
            Claim claim = claimsDao.save(new Claim());

            log.debug("claim saved, claim id: " + claim.getId());
        }
    }
}
