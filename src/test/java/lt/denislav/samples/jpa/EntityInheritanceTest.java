package lt.denislav.samples.jpa;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.util.Date;

import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.domain.HomeClaim;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test shows how inheritance works.
 * 
 * {@link HomeClaim} is persisted and loaded. 
 * You can play with {@link @Inheritance} annotations on {@link Claim} entity
 * to see how different inheritance strategies work.
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityInheritanceTest extends BaseTest {
	
	private Long claimId;

	@Autowired
	private ClaimsDao claimsDao;
	
	/**
	 * {@link HomeClaim} entity is persisted.
	 */
	@Before
	public void prepareClaim() {
		HomeClaim claim = new HomeClaim();	
		claim.getAdress().setAddressLine("Vytauto g. 11, Vilnius");
		claim.setStatus(ClaimStatus.OPEN);
		claim.setDate(new Date());
		
		claim = (HomeClaim) claimsDao.save(claim);
		claimId = claim.getId();	
		
//		org.hsqldb.util.DatabaseManagerSwing.main(new String[] {"--url",  "jdbc:hsqldb:mem:ipb", "--noexit"	});
	}
		
	/**
	 * {@link HomeClaim} is loaded.
	 */
	@Test
	public void loadClaim() {		
		Claim claim = claimsDao.findById(claimId);

		log.info("loaded claim" + claim);
		
		assertThat(claim, instanceOf(HomeClaim.class));
	}
}
