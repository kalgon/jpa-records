# Java 14 Records and JPA

## Introduction

This small project tests what is currently possible wrt using records with JPA. Only Hibernate has been tested for now.

## Records used as projections

### Inside queries

Projections seem to be supported without any modification to existing JPA providers.

    select new my.company.MyRecord(e.x, sum(c.y)) from MyEntity e join e.children c
    
### Outside queries

If in-query projections are not powerful enough, they can be done outside of the query in standard java code or by using a library ([mapstruct](https://github.com/mapstruct/mapstruct/issues/2014), beanutils, dozer...).
    
## Records used as attributes

### @Embeddable/@Embedded records

Records cannot be used as `@Embeddable`/`@Embedded`/`@EmbeddedId` as they don't define a default constructor. Hibernate won't even persist them.

It would be really nice if a records could be used and annotated like any other embeddable object:

    @Embeddable
    public record Fraction(@Basic(optional = false) BigInteger numerator, @Basic(optional = false) BigInteger denominator) {}
    
Alternatively, the record could be declared in orm.xml:

    <entity-mappings xmlns="http://xmlns.jcp.org/xml/ns/persistence/orm" version="2.1">
        <embeddable class="com.mycompany.Fraction">
            <attributes>
                <basic name="numerator" optional="false" />
                <basic name="denominator" optional="false" />
            </attributes>
        </embeddable>
    </entity-mappings>

### With AttributeConverter

Single-valued records could be used as attributes but require a standard JPA `AttributeConverter`.

Multi-valued records could be used as attributes but require a vendor-specific adapter (like Hibernate `UserType`) as standard `AttributeConverter`s [do not support multiple columns](https://github.com/eclipse-ee4j/jpa-api/issues/105).

## Records used as entities

Records cannot be used as `@Entity` as they don't define a default constructor: they can be persisted but not read back.
Using records as entities implies that those entities are immutable: identifiers cannot be generated (`@GeneratedValue`) and must be passed in the constructor.

Question: What if an immutable record entity contains mutable attributes? Won't `hashCode()`/`equals()` be impacted?

## Conclusions

The biggest problem for JPA providers seems to be that records do not have a default constructor.
If JPA providers could add some flexibility in how objects are instantiated/initialized, records could easily be supported.
Supporting initialization through non-default constructors could also be beneficial for Object-Oriented Design: entities could have a single constructor and fields could be made final (JPA could even infer that `@Column.updatable = false` for final fields).
Some inspiration could be found from JSON-B which allows non-default constructors to be used (see `@JsonbCreator`).

Another track to keep an eye on is the (potential) new [serialization mechanism](https://cr.openjdk.java.net/~briangoetz/amber/serialization.html) which would allow better/easier state extraction and object creation from that state.  