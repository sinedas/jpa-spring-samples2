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
import lt.denislav.samples.jpa.domain.PersonDetail;
import lt.denislav.samples.jpa.dto.ClaimDTO;
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
import static org.hamcrest.Matchers.instanceOf;

/**
 * Various selects usage in jpql.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SelectInJpqlTest extends BaseTest {

	@Autowired
	ClaimsDao claimsDao;

	@PersistenceContext
	private EntityManager entityManager;

	private Long claimId;
	
	@Before
	public void prepareClaims() {
		for (int i = 0; i < 2; i++) {
			Claim claim = new Claim();
			claim.setStatus(ClaimStatus.NOTIFICATION);			
			claim.getPersons()
					.add(new Person("John " + i).withPersonDetail(new PersonDetail()));
			claim.getPersons()
					.add(new Person("Peter " + i).withPersonDetail(new PersonDetail()));
			claimsDao.save(claim);
			
			claimId = claim.getId();
		}
		
		Claim claim = new Claim();
		claimsDao.save(claim);

		entityManager.flush();
		entityManager.clear();
	}

	@After
	public void tearDown() {
		entityManager.createQuery("delete from Person").executeUpdate();
		entityManager.createQuery("delete from PersonDetail").executeUpdate();
		entityManager.createQuery("delete from Claim").executeUpdate();
	}
	
	/**
	 * Change hibernate.max_fetch_depth to 0 and see 
	 * how sql statements change.
	 */
	@Transactional
	@Test
	public void usageOfFindById() {
		Claim claim = entityManager.find(Claim.class, claimId);
		
		log.debug("Found claim: " + claim);

		assertThat(claim, instanceOf(Claim.class));
	}

	/**
	 * Usage of select statement.
	 */
	@Transactional
	@Test
	public void usageOfSelect() {
		Query query = entityManager.createQuery("select c from Claim c");
		
		@SuppressWarnings("unchecked")
		List<Claim> list = query.getResultList();
		
		log.debug("Found claims: " + list);
		assertThat(list.size(), equalTo(3));
	}
	
	/**
	 * Usage of fetch in jpql query.
	 * By default left join is OUTER.
	 */
	@Transactional
	@Test
	public void usageOfStatementFetch() {
		//
		//https://vladmihalcea.com/jpa-hibernate-query-hints/
		//https://vladmihalcea.com/jpql-distinct-jpa-hibernate/
		Query query = entityManager.createQuery("select distinct c from Claim c left join fetch c.persons p ");// +
//				"");
	//	" left join fetch p.personDetail");
				
		@SuppressWarnings("unchecked")
		List<Claim> list = query.getResultList();
		
		log.debug("Found claims: " + list);
		assertThat(list.size(), equalTo(3));
	}
		
	/**
	 * Previous tests but inner join is used.
	 * By fefault join is INNER
	 */
	@Transactional
	@Test
	public void usageOfStatementInnerJoin() {
		Query query = entityManager.createQuery("select c from Claim c join fetch c.persons p " + // "inner" is optinal
//				"");
		" join fetch p.personDetail");
				
		@SuppressWarnings("unchecked")
		List<Claim> list = query.getResultList();
		
		log.debug("Found claims count: " + list.size());
		assertThat(list.size(), equalTo(3));
	}
	
	/**
	 * Usage of new constructor in jpql.
	 */
	@Transactional
	@Test
	public void usageOfConstructorInSelect() {
		Query query = entityManager.createQuery("select new " + ClaimDTO.class.getName() + "(c.status) from Claim c ");
				
		@SuppressWarnings("unchecked")
		List<ClaimDTO> list = query.getResultList();
		log.debug("Found claims dto: " + list);
		
		assertThat(list.size(), equalTo(3));
	}
	
}
