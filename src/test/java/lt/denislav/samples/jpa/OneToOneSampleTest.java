package lt.denislav.samples.jpa;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import lt.denislav.samples.jpa.domain.Claim;
import lt.denislav.samples.jpa.domain.Person;
import lt.denislav.samples.jpa.domain.PersonDetail;
import lt.denislav.samples.jpa.repository.PersonDetailsRepository;
import lt.denislav.samples.jpa.repository.PersonsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


/**
 * {@link OneToOne} association test. Also example of {@link CascadeType}, {@link FetchType} usage.
 *
 * <p>
 * Uncomment @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY) in {@link Claim}.
 * <ul>
 * <li>
 * #{OneToOneSampleTest{@link #loadPerson()}} should fail.
 * </li>
 * <li>
 * #{OneToOneSampleTest{@link #loadPersonInTransaction()} should pass.
 * </li>
 * </p>
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class OneToOneSampleTest extends BaseTest {

    @Autowired
    private PersonsRepository personsRepository;

    @Autowired
    private PersonDetailsRepository personDetailsRepository;

    private Long personId;

    @Autowired
    private EntityManager entityManager;

    /**
     * Saves {@link Person} with {@link lt.denislav.samples.jpa.domain.PersonDetail}.
     * <p>
     * Remove cascade=CascadeType.ALL from {@link OneToOne} {@link Person#getPersonDetail()}
     * to see that detail is not saved together with claim.
     */
    @Before
    public void preparePerson() {
        Person person = new Person();
        person.setName("Ostap Bender");

        PersonDetail personDetail = new PersonDetail();
        personDetail.setPersonalCode("3333");

        person.setPersonDetail(personDetail);

        personDetailsRepository.saveAndFlush(personDetail);
        person = personsRepository.saveAndFlush(person);
        personId = person.getId();

        entityManager.clear();

        logDashes("before");
    }

    @After
    public void clearPersons() {
        logDashes("after");

        personsRepository.deleteAll();
    }

    @Test
    public void justLoadPerson() {
        Person person = personsRepository.findById(personId).get();

        log.info("Loaded person: " + person);
    }

    /**
     * If uncomment @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY) in {@link Person#getPersonDetail()},
     * test will fail because transaction is not started, entities can be loaded lazily only in transaction.
     */
    @Test
    public void loadPersonWithDetails() {
        Person person = personsRepository.findById(personId).get();
        PersonDetail detail = person.getPersonDetail();

        log.info("Loaded person: " + person);
        log.info("Loaded person detail : " + detail);

        assertThat(detail, notNullValue());
    }

    /**
     * If uncomment @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY) in {@link Person#getPersonDetail()},
     * test will not fail because transaction is started, and entities can be loaded lazily only in transaction.
     */
    @Transactional(readOnly = true)
    @Test
    public void loadPersonWithDetailsInTransaction() {
        Person person = personsRepository.findById(personId).get();
        PersonDetail detail = person.getPersonDetail();

        assertThat(detail, notNullValue());

        log.debug("Loaded person : " + person);
        log.debug("Loaded person : " + detail);
    }
}
