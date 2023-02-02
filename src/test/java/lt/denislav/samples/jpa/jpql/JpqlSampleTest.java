package lt.denislav.samples.jpa.jpql;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lt.denislav.samples.jpa.BaseTest;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.ClaimStatus;
import lt.denislav.samples.jpa.domain.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Variuous statements in jpql queries.
 * 
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpqlSampleTest extends BaseTest{

	@Autowired
	ClaimsDao claimsDao;

	@PersistenceContext
	private EntityManager entityManager;
		
	@Before
	public void prepareTwoClaimsWithDifferentSizeOfPersons() {
		Claim claim1 = new Claim();
		claim1.setStatus(ClaimStatus.NOTIFICATION);
		claim1.setPersons(List.of(new Person("John"), new Person("Wiljam")));
		
		Claim claim2 = new Claim();
		claim2.setStatus(ClaimStatus.OPEN);
		claim2.setPersons(List.of(new Person("Peter")));
		
		Claim claim3 = new Claim();	
		claim3.setStatus(ClaimStatus.CLOSED);
		
		claimsDao.save(claim1);
		claimsDao.save(claim2);
		claimsDao.save(claim3);

		entityManager.flush();

		logDashes("before");
	}

	@After
	public void tearDown() {
		entityManager.createQuery("delete from Person").executeUpdate();
		entityManager.createQuery("delete from Claim").executeUpdate();
	}
		
	/**
	 * Usage of "EMPTY" in jpql.
	 */
	@Transactional(readOnly = true)
	@Test
	public void usageOfStatementEmpty() {		
		Query query = entityManager.createQuery("select c from Claim c where c.persons is EMPTY");
				
		@SuppressWarnings("unchecked")
		List<Claim> result = query.getResultList();
		
		log.debug("Found claims: " + result);
		assertThat(result.size(), equalTo(1));
	}
	
	/**
	 * Usage of statement "IN" in jpql
	 */
	@Transactional(readOnly = true)
	@Test
	public void usageOfStatementIn() {
		
		Query query = entityManager.createQuery("select c from Claim c where c.status IN (:statuses)");
		query.setParameter("statuses", List.of(ClaimStatus.NOTIFICATION, ClaimStatus.OPEN));
		
		@SuppressWarnings("unchecked")
		List<Claim> result = query.getResultList();
		
		log.debug("Found claims: " + result);
		assertThat(result.size(), equalTo(2));
	}
	
	
	
	

}
