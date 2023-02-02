package lt.denislav.samples.jpa;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lt.denislav.samples.jpa.dao.ClaimsDao;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.Person;
import lt.denislav.samples.jpa.repository.ClaimsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * {@link OneToMany} association usage example.
 * Eager/Lazy fetching
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneToManySampleTest extends BaseTest {

	@Autowired
	private ClaimsDao claimsDao;

	@Autowired
	private ClaimsRepository claimsRepository;

	private Long claimId;

	@Autowired
	private EntityManager entityManager;
	/**
	 * Claim with 2 persons is created.
	 * 
	 * You can play with {@link OneToMany#cascade()} property to
	 * see how cascades work.
	 */
	@Before
	public void prepareClaim() {
		Claim claim = new Claim();
		claim.getPersons().add(new Person("John Smith"));
		claim.getPersons().add(new Person("Anne Smith"));
		
		claim = claimsRepository.saveAndFlush(claim);
		//claim = claimsRepository.save(claim);
		claimId = claim.getId();

		entityManager.clear();

		logDashes(" Before ");
	}

	@After
	@Transactional
	public void clearClaims() {
		logDashes(" After ");

		claimsRepository.deleteAll();
	}

	@Test
	public void justLoadClaim() {
		Claim claim = claimsRepository.findById(claimId).get();

		log.info("loaded claim with id: "  + claim.getId());
	}

	@Test
	@Transactional(readOnly = true)
	public void loadClaimAndGetPersonsInTransaction() {
		Claim claim = claimsRepository.findById(claimId).get();

		log.info("---");
		log.info("loaded persons: "  + claim.getPersons());

		assertThat(claim.getPersons().size(), equalTo(2));
	}

	/**
	 * Claim is loaded.
	 * If to change {@link OneToMany#fetch()} property to {@link FetchType#LAZY} in {@link Claim#getPersons()}
	 * test will fail because lazy fetching work only in transaction scope.
	 */
	@Test
	//@Transactional(readOnly = true)
	public void loadClaimAndGetPersons() {
		Claim claim = claimsRepository.findById(claimId).get();

		log.info("---");
		log.info("loaded persons: "  + claim.getPersons());

		assertThat(claim.getPersons().size(), equalTo(2));
	}

	
	/**
	 * 1st person is removed from Claim.
	 * 
	 * If to set {@link OneToMany#orphanRemoval} to true in {@link Claim#getPersons()}
	 * not only foreign key will be set to null, but also {@link Person}
	 * will be removed.
	 */
	@Transactional
	@Test	
	public void removePerson() {
		Claim claim = claimsRepository.findById(claimId).get();
		
		log.info("loaded persons: "  + claim.getPersons());
		
		claim.getPersons().remove(0);
	}
}
