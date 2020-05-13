package jakarta.persistence.records;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public record ImmutablePerson(@Id long id, @Basic String name) {
}
