package lt.denislav.samples.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lt.denislav.samples.jpa.domain.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PersonsDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Person save(Person person) {
        if (null == person.getId()) {
            entityManager.persist(person);
        } else {
            person = entityManager.merge(person);
        }

        return person;
    }

    @Transactional(readOnly = true)
    public Person findById(Long id) {
        return entityManager.find(Person.class, id);
    }

    @Transactional
    public void delete(Long id) {
        entityManager.remove( entityManager.find(Person.class, id));
    }
}
