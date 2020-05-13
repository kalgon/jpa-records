package jakarta.persistence.records;

import java.math.BigDecimal;

public record OrderProjection(Order order, BigDecimal totalAmount) {
}
