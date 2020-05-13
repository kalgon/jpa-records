package jakarta.persistence.records;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SocialSecurityNumberAttributeConverter implements AttributeConverter<SocialSecurityNumber, Long> {

    @Override
    public Long convertToDatabaseColumn(SocialSecurityNumber socialSecurityNumber) {
        return socialSecurityNumber == null ? null : socialSecurityNumber.value();
    }

    @Override
    public SocialSecurityNumber convertToEntityAttribute(Long value) {
        return value == null ? null : new SocialSecurityNumber(value);
    }
}
