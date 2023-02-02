package lt.denislav.samples.jpa.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * Base entity which is extended by all entities.
 * Contains generic fields: id, version. Additional generic fields could be added.
 * 
 * In samples is used to show how entities annotated with {@link MappedSuperclass} work.
 */
@MappedSuperclass
public class BaseEntity {

	/**
	 * Primary key. For field default value generation {@link GenerationType#AUTO} is used.
	 * 
	 * Lines with other strategies can be uncommented to experiment with them.
	 */
	@Id
	@GeneratedValue
//	@GeneratedValue(strategy=GenerationType.IDENTITY )
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="custom_sequence")
//	@SequenceGenerator(name="custom_sequence", sequenceName="custom_sequence",  allocationSize=5)
//	@GeneratedValue(strategy=GenerationType.TABLE, generator="table_sequence")
//	@TableGenerator(table="table_sequence", name = "table_sequence", allocationSize=2)
	private Long id;

//	@Id
//	@GeneratedValue(generator = "uuid2")
//	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//	@Column(name = "id", length = 36)
//	@Type(type = "org.hibernate.type.UUIDCharType")
	//private UUID id;

	
	/**
	 * Version field, which serves as entities optimistic lock value.
	 *
	 */
	@Version
	private Integer version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
