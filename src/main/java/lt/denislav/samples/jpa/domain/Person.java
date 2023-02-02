package lt.denislav.samples.jpa.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import javax.persistence.Transient;
import org.hibernate.mapping.OneToMany;


/**
 * Entity containing person information.
 * <p>
 * In this domain person is related to concrete {@link Claim}
 * (claim contains several entities), but in general can be used in other prospects.
 * <p>
 * In samples used to show {@link OneToMany} usage.
 */
@Entity
public class Person extends BaseEntity {

    public Person() {
        super();
    }

    public Person(String name) {
        super();
        this.name = name;
    }

    public Person withPersonDetail(PersonDetail personDetail) {
        this.setPersonDetail(personDetail);
        return this;
    }

    /**
     * First name of person.
     */
    private String name;

    @OneToOne
    //@OneToOne(cascade = CascadeType.ALL)
    //@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColumn(name = "personDetailId")
    //@Transient
    private PersonDetail personDetail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PersonDetail getPersonDetail() {
        return personDetail;
    }

    public void setPersonDetail(PersonDetail personDetail) {
        this.personDetail = personDetail;
    }

    @Override
    public String toString() {
        return "Person [id=" + getId() + ", name=" + name + "]";
    }
}
