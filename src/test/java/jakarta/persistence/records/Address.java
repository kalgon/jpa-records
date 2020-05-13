package jakarta.persistence.records;

import javax.persistence.Embeddable;

@Embeddable
public record Address(String street, String zipCode, String city, String country) {
}
