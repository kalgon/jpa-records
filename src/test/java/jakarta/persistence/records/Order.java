package jakarta.persistence.records;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> lines = new ArrayList<>();

    public void addLine(int quantity, Product product) {
        lines.add(new OrderLine(this, quantity, product));
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return lines.stream().map(OrderLine::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
