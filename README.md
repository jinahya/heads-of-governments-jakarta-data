# heads-of-governments-jakarta-data

Jakarta Data repositories, `OfficeRepository` and `HeadRepository`, for the entities of
[heads-of-governments-persistence](../heads-of-governments-persistence), implemented by hibernate-processor and tested
with `@QuarkusTest`.

## Why each repository extends `CrudRepository` itself

The repositories share a generic base, `_Repository<T, U>`, which extends `BasicRepository<T, U>`. Each repository
still names `CrudRepository` with its own types, next to the base:

```java
@Repository
public interface HeadRepository
        extends _Repository<Head, Long>, CrudRepository<Head, Long> {
```

Without the second `CrudRepository<Head, Long>`, the build fails, whether the base extends `BasicRepository<T, U>` or
`CrudRepository<T, U>`:

```
HeadRepository.java:[18,8] repository does not have a well-defined primary entity type for inherited member 'deleteById'
```

The inherited `save`, `deleteById`, `findById`, and so on need the repository's _primary entity type_. The Jakarta
Data specification, at
[1.0.1](https://github.com/jakartaee/data/blob/1.0.1/spec/src/main/asciidoc/repository.asciidoc), the version this
module uses, says how a repository declares it:

> Users of Jakarta Data declare a primary entity type for a repository by inheriting from a built-in repository super
> interface, such as `BasicRepository`, and specifying the primary entity type as the first type variable.

It does not say whether a built-in supertype inherited through a generic interface of one's own, as
`BasicRepository<T, U>` through `_Repository<Head, Long>`, counts. Jakarta Data 1.1 says it does not; since 1.1.0-M4,
its [specification](https://github.com/jakartaee/data/blob/main/spec/src/main/asciidoc/repository.asciidoc) adds:

> A superinterface of a repository interface is not considered to have a well-defined primary entity type, and
> so--unless the superinterface is one of the built-in generic repository supertypes--each of its operations must have a
> signature which explicitly determines the entity type it acts on, according to the rules given above.

`_Repository` is such a superinterface, so it declares no primary entity type for `HeadRepository`;
`CrudRepository<Head, Long>`, named on `HeadRepository` itself, does. hibernate-processor already follows that rule,
and its error message uses its words. Spring Data has no such rule and resolves the generics at run time, so
[heads-of-governments-spring-data](../heads-of-governments-spring-data) needs only `_Repository<Head, Long>`.

hibernate-processor, which generates the repositories at compile time, looks for the primary entity type in
`org.hibernate.processor.annotation.AnnotationMetaEntity#primaryEntity(javax.lang.model.element.TypeElement)`
(hibernate-processor 7.4.13.Final; unchanged on
[`main`](https://github.com/hibernate/hibernate-orm/blob/main/tooling/metamodel-generator/src/main/java/org/hibernate/processor/annotation/AnnotationMetaEntity.java)
and in 8.0.0.Beta4):

1. Among the superinterfaces of `HeadRepository`, it finds `_Repository<Head, Long>`. That is not `DataRepository`,
   `BasicRepository`, or `CrudRepository`, so it recurses.
2. It recurses on the declaration `_Repository<T, U>`, not on the use `_Repository<Head, Long>`, so `T = Head` is lost.
3. Inside `_Repository`, it finds `BasicRepository<T, U>`. Its first type argument is the type variable `T`, not a
   declared type, so nothing is found.

When the recursion finds nothing, the lookup goes on to the next superinterface. `CrudRepository<Head, Long>`, named
directly, has `Head` as its first type argument, and the methods inherited through `_Repository` resolve from there.

The `@Find` methods are not affected. As 1.1 requires of the operations of such a superinterface, their signatures
determine the entity type; hibernate-processor reads each as a member of the repository, so a
`@Find List<T> findAll(Order<T>)` declared in `_Repository` works as `List<Head>` for `HeadRepository` without
`CrudRepository<Head, Long>`.

