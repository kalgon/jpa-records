package jakarta.persistence.records;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private SocialSecurityNumber ssn;

    private Address address;

    public SocialSecurityNumber getSsn() {
        return ssn;
    }

    public void setSsn(SocialSecurityNumber ssn) {
        this.ssn = ssn;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }
}
