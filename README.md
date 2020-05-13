# Java 14 Records and JPA

## Introduction

This small project tests what is currently possible wrt using records with JPA. Only Hibernate has been tested for now.

## Records used as projections

Projections seem to work without any modification to existing JPA providers.

    select new my.company.MyRecord(e.x, sum(e.y)) from MyEntity e
    
## Records used as attributes

### @Embeddable/@Embedded records

Records cannot be used as `@Embeddable`/`@Embedded` as they don't define a default constructor. Hibernate won't even persist them.

### With AttributeConverter

Single-valued records could be used as attributes but require a specific standard JPA `AttributeConverter`.

Multi-valued records could be used as attributes but require a specific vendor-specific adapter (like Hibernate `UserType`) as standard `AttributeConverter`s  do not support multiple columns.

### Entities

Records cannot be used as (immutable) `@Entity` as they don't define a default constructor: they can be persisted but not read back.

Question: What if an immutable record entity contains mutable attributes? Won't `hashCode()`/`equals()` be impacted?

## Conclusions

The biggest problem for JPA providers seems to be that records do not have a default constructor.
If JPA providers could add some flexibility in how objects are instantiated/initialized, records could easily be supported.
Supporting initialization through non-default constructors could also be beneficial for Object-Oriented Design: entities could have a single constructor and fields could be made final.