package lt.denislav.samples.jpa.jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lt.denislav.samples.jpa.BaseTest;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


/**
 * Usage of bulk queries sample.
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestEntityManager
public class BulkStatements extends BaseTest{

	@Autowired
	ClaimsDao claimsDao;

	@PersistenceContext
	private EntityManager entityManager;

	private Long claimId;

	/**
	 * 2 {@link Claim} entities in different states are created.
	 */
	@Before
	public void prepareThreeClaimsInDifferentStatuses() {
		Claim claim1 = new Claim();
		claim1.setStatus(ClaimStatus.NOTIFICATION);
		
		Claim claim2 = new Claim();
		claim2.setStatus(ClaimStatus.OPEN);
		
		Claim claim3 = new Claim();
		claim3.setStatus(ClaimStatus.CLOSED);
				
		claimsDao.save(claim1);
		claimsDao.save(claim2);
		claimsDao.save(claim3);
	}
		
	/**
	 * Changing status of all claims to closed with bulk update.
	 */
	@Transactional
	@Test
	public void usageOfStatementUpdate() {				
		Query query = entityManager.createQuery("update Claim c SET c.status = :status");
		query.setParameter("status",ClaimStatus.CLOSED);
		query.executeUpdate();
		entityManager.flush();
		
		query = entityManager.createQuery("select c from Claim c where c.status IN (:statuses)");
		query.setParameter("statuses", List.of(ClaimStatus.CLOSED));
		
		@SuppressWarnings("unchecked")
		List<Claim> result = query.getResultList();
		
		log.debug("Found claims: " + result);
		assertThat(result.size(), equalTo(3));
	}
	
	/**
	 * Deletion of all claims with bulk update.
	 */
	@Transactional
	@Test
	public void usageOfStatementDelete() {
		Query query = entityManager.createQuery("delete Claim c");		
		query.executeUpdate();
		
		query = entityManager.createQuery("select c from Claim c");		
		
		@SuppressWarnings("unchecked")
		List<Claim> result = query.getResultList();
		
		log.debug("Found claims: " + result);
		assertThat(result.size(), equalTo(0));
		
	}
	

	
}
