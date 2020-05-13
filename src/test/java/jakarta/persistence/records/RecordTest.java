package jakarta.persistence.records;

import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class RecordTest {

    /**
     * Records do not work as @Embeddable/@Embedded as they don't have a default constructor.
     * Single-valued records can be used as attributes but require a specific AttributeConverter.
     * Multi-valued records could be used as attributes but require vendor-specific adapters (like hibernate UserType).
     */
    @Test
    public void testRecordsForAttributes() {
        Person person = new Person();
        person.setSsn(new SocialSecurityNumber(42L)); // requires AttributeConverter
        //person.setAddress(new Address("Sunset Blvd 1", "CA 90049", "Los Angeles", "USA")); // not working (missing default constructor)

        entityManager.getTransaction().begin();
        entityManager.persist(person);
        entityManager.getTransaction().commit();

        entityManager.clear();

        entityManager.getTransaction().begin();
        Person reloadedPerson = entityManager.find(Person.class, person.getId());
        Assertions.assertEquals(person.getSsn(), reloadedPerson.getSsn());
        entityManager.getTransaction().rollback();
    }

    /**
     * Records work as projections.
     */
    @Test
    public void testRecordsForProjections() {

        Product cocaCola = new Product("Coca-Cola", new BigDecimal("1.20"));
        Product fanta = new Product("Fanta", new BigDecimal("1.10"));
        Product sprite = new Product("Sprite", new BigDecimal("1.30"));

        Order order = new Order();
        order.addLine(1, cocaCola);
        order.addLine(2, fanta);
        order.addLine(3, sprite);

        entityManager.getTransaction().begin();
        entityManager.persist(cocaCola);
        entityManager.persist(fanta);
        entityManager.persist(sprite);
        entityManager.persist(order);
        entityManager.getTransaction().commit();

        entityManager.clear();

        entityManager.getTransaction().begin();
        OrderProjection projection = entityManager
                .createQuery("select new jakarta.persistence.records.OrderProjection(o, sum(l.quantity * p.price)) from Order o join o.lines l join l.product p where o.id = :id", OrderProjection.class)
                .setParameter("id", order.getId())
                .getSingleResult();
        Assertions.assertEquals(order.getTotal(), projection.totalAmount());
        entityManager.getTransaction().rollback();
    }

    /**
     * Record entities can be written to the database but not read back (because of missing default constructor).
     */
    @Test
    public void testRecordsForEntities() {
        entityManager.getTransaction().begin();
        entityManager.persist(new ImmutablePerson(2L, "George Washington")); // ok
        entityManager.getTransaction().commit();

        entityManager.clear();

        entityManager.getTransaction().begin();
        // entityManager.find(ImmutablePerson.class, 2L); // not working
        entityManager.getTransaction().commit();
    }


    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void createEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("records");
    }

    @AfterAll
    public static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    @BeforeEach
    public void createEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeEntityManger() {
        entityManager.close();
    }
}
