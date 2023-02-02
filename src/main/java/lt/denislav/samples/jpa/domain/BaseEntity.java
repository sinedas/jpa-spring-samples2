package lt.denislav.samples.jpa.domain;

import javax.persistence.*;

/**
 * Base entity which is extended by all entities.
 * Contains generic fields: id, version. Additional generic fields could be added.
 * <p>
 * In samples is used to show how entities annotated with {@link MappedSuperclass} work.
 */
@MappedSuperclass
public class BaseEntity {

    /**
     * Primary key. For field default value generation {@link GenerationType#AUTO} is used.
     * <p>
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
