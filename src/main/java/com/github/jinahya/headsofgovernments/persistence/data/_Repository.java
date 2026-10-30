package com.github.jinahya.headsofgovernments.persistence.data;

/**
 * The repository the repositories of this package extend, with what they have in common.
 * <p>
 * It is not annotated {@link jakarta.data.repository.Repository}, so nothing is generated for it.
 * It extends {@link jakarta.data.repository.BasicRepository} with the type variables, which
 * declares no primary entity type for the repositories: Jakarta Data 1.1 does not consider a
 * superinterface other than the built-in ones to have one, a case 1.0 leaves open, and Hibernate
 * Data Repositories follows 1.1. So each repository also extends
 * {@link jakarta.data.repository.CrudRepository} itself, with its own types, e.g.,
 * {@code CrudRepository<Head, Long>}; see the {@code README.md}.
 *
 * @param <T> the type of the entities.
 * @param <U> the type of the identifiers of the entities.
 */
interface _Repository<T, U>
        extends jakarta.data.repository.BasicRepository<T, U> {

}
