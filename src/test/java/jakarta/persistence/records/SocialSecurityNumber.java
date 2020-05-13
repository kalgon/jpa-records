package jakarta.persistence.records;

public record SocialSecurityNumber(long value) {

    public SocialSecurityNumber {
        if (value < 0) {
            throw new IllegalArgumentException("value must be positive");
        }
    }
}
