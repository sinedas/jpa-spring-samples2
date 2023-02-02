package lt.denislav.samples.jpa;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.Policy;

import lt.denislav.samples.jpa.repository.ClaimsRepository;
import lt.denislav.samples.jpa.repository.PolicyRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;


/**
 * {@link ManyToOne} association test. 
 * Also example of using jpql and aggregation functions.
 * 
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ManyToOneSampleTest extends BaseTest{

	@Autowired
	private ClaimsDao claimsDao;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ClaimsRepository claimsRepository;

	@Autowired
	private PolicyRepository policyRepository;

	private Claim claim1;

	private Claim claim2;
	
	/**
	 * Saves 2 {@link Claim} associated to the same {@link Policy}.
	 */
	@Before
	public void prepareClaims() {
		Policy policy = new Policy();
		policy.setPolicyNumber("A01");
		
		claim1 = new Claim();
		claim1.setPolicy(policy);
		
		claim2 = new Claim();
		claim2.setPolicy(policy);
		
		policy.getClaims().add(claim1);
		policy.getClaims().add(claim2);
		
		claim1 = claimsDao.save(claim1);
		claim2 = claimsDao.save(claim2);

		entityManager.flush();
		entityManager.clear();

		logDashes(" Before ");
	}

	@After
	public void clearClaims() {
		logDashes(" After ");

		policyRepository.deleteAll();
		claimsRepository.deleteAll();

		entityManager.flush();
	}
	
	/**
	 * Finds claims by policy number.
	 */
	@Transactional(readOnly = true)
	@Test
	public void findClaimsByPolicyNumber() {
		Query query = entityManager.createQuery("select c from Claim c where c.policy.policyNumber = :policyNumber");
		query.setParameter("policyNumber", "A01");
		
		@SuppressWarnings("unchecked")
		List<Claim> result = query.getResultList();
		
		log.info("Found claims: " + result);
		assertThat(result.size(), equalTo(2));
	}
	
	/**
	 * Finds claims count by policy number.
	 */
	@Transactional(readOnly = true)
	@Test
	public void findClaimsCountByPolicyNumber() {
		Query query = entityManager.createQuery("select count(c) from Claim c where c.policy.policyNumber = :policyNumber");
		query.setParameter("policyNumber", "A01");
		
		Long result = (Long)query.getSingleResult();
		
		log.debug("Found claims sum: " + result);
		assertThat(result, equalTo(2L));
	}

	
	
	

}
