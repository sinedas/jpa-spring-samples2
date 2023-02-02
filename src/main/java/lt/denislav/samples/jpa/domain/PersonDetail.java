package lt.denislav.samples.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity containing person details information.
 * 
 * In samples is used to show {@link OneToOne} association usage.
 */
@Entity
public class PersonDetail extends BaseEntity {

	public PersonDetail() {
		super();
	}

	public PersonDetail(String personalCode) {
		super();
		this.personalCode = personalCode;
	}

	/**
	 * Person identification code.
	 */
	private String personalCode;
	
	public String getPersonalCode() {
		return personalCode;
	}

	public void setPersonalCode(String personalCode) {
		this.personalCode = personalCode;
	}

	@Override
	public String toString() {
		return "Person detail; [id=" + getId() + ", personalCode=" + personalCode + "]";
	}
}
