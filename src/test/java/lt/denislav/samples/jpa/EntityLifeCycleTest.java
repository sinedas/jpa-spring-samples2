package lt.denislav.samples.jpa;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.domain.Person;
import lt.denislav.samples.jpa.domain.PersonDetail;
import lt.denislav.samples.jpa.dto.ClaimDTO;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Test explaining how entity lifecycle works.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EntityLifeCycleTest extends BaseTest {
		
	private Long claimId;

	@Autowired
	private ClaimsDao claimsDao;

	@Autowired
	private ClaimsRepository claimsRepository;

	@Autowired
	private EntityManager entityManager;
	
	/**
	 * {@link Claim} is persisted.
	 */
	@Before
	public void prepareClaim() {
		Claim claim = new Claim();
		claim.setStatus(ClaimStatus.NOTIFICATION);
		claim.setDate(new Date());

		claim.getPersons()
				.add(new Person("John" ));
		claim = claimsRepository.save(claim);

		claimId = claim.getId();

		entityManager.flush();
		entityManager.clear();

		log.info("claim id: " + claimId);
		logDashes("before");
	}

	@After
	public void deleteClaim() {
		logDashes("after");
		claimsRepository.deleteAll();
	}

	/**
	 * Attached {@link Claim} entity is updated on the end of transaction.
	 */
	@Transactional
	@Test
	public void findAndChangeInTransaction() {
		Claim claim = claimsRepository.findById(claimId).get();

		log.info("loaded claim: " + claim);

		claim.setStatus(ClaimStatus.OPEN);
		claim.getPersons().get(0).setName("bla");

		claim = claimsRepository.findById(claimId).get();

		log.info("loaded person: "  + (claim.getPersons().get(0)));
	}
	
	/**
	 * Detached {@link Claim} entity is updated by {@link EntityManager#merge(Object)}.
	 */
	@Transactional
	@Test
	public void mergeDetachedEntity() {		
		Claim claim = claimsDao.findById(claimId);
		claim.setStatus(ClaimStatus.OPEN);
		
		// Persistence context is cleared, all entities become detached.
		entityManager.clear();
		
		// Entity merged
		claim = entityManager.merge(claim);
	}
	
	/**
	 * In {@link FlushModeType#AUTO} mode, attached {@link Claim} entity is
	 * updated before executing jpql query.	
	 */
	@Transactional
	@Test
	public void queryIsCalledInAutoFlushMode() {
		Claim claim = claimsDao.findById(claimId);
		claim.setStatus(ClaimStatus.CLOSED);				
				
		Query query = entityManager.createQuery("select  new " + ClaimDTO.class.getName() + "(c.status) from Claim c");
		query.setFlushMode(FlushModeType.AUTO); // No needed to set. It's default value used for better reading.

		ClaimDTO claimDTO = (ClaimDTO)query.getResultList().get(0);
		
		log.info(claimDTO);
		assertThat(claimDTO.getStatus(), equalTo(ClaimStatus.CLOSED));
	}
	
	/**
	 * In {@link FlushModeType#COMMIT} mode, attached {@link Claim} entity is
	 * updated at the end of transaction.
	 */
	@Transactional
	@Test
	public void queryIsCalledInCommitFlushMode() {		
		Claim claim = claimsDao.findById(claimId);
		claim.setStatus(ClaimStatus.CLOSED);				
						
		Query query = entityManager.createQuery("select new " + ClaimDTO.class.getName() + "(c.status) from Claim c");
		query.setFlushMode(FlushModeType.COMMIT);

		ClaimDTO claimDTO = (ClaimDTO)query.getResultList().get(0);
		
		log.info(claimDTO);
		assertThat(claimDTO.getStatus(), equalTo(ClaimStatus.NOTIFICATION));
	}
}