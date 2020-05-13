package jakarta.persistence.records;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderLine {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Order order;

    @Basic
    private int quantity;

    @ManyToOne
    private Product product;

    OrderLine() {
        // for JPA
    }

    OrderLine(Order order, int quantity, Product product) {
        this.order = order;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getTotal() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }
}
