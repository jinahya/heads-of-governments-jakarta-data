package com.github.jinahya.headsofgovernments.persistence.data;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.Objects;

/**
 * An abstract {@link io.quarkus.test.junit.QuarkusTest} of a repository, against
 * {@code heads_of_governments.sqlite3}, a test resource, as {@code application.yaml}
 * configures it; no {@code persistence.xml}.
 * <p>
 * Each concrete subclass is annotated {@link io.quarkus.test.junit.QuarkusTest} itself; Quarkus
 * does not make a test of a class that only inherits it.
 *
 * @param <T> the type of the repository.
 * @param <U> the type of the entities of the repository.
 * @param <V> the type of the identifiers of the entities.
 */
abstract class _Repository_QuarkusTest<T extends _Repository<U, V>, U, V> {

    // ---------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with the specified classes.
     *
     * @param repositoryClass the class of the repository.
     * @param entityClass     the class of the entities of the repository.
     * @param idClass         the class of the identifiers of the entities.
     */
    _Repository_QuarkusTest(final Class<T> repositoryClass, final Class<U> entityClass,
                            final Class<V> idClass) {
        super();
        this.repositoryClass = Objects.requireNonNull(repositoryClass, "repositoryClass is null");
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass is null");
        this.idClass = Objects.requireNonNull(idClass, "idClass is null");
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Returns the instance of the repository, selected from {@link #repositoryInstances}.
     *
     * @return the instance of the repository.
     */
    T repositoryInstance() {
        return repositoryInstances.select(repositoryClass).get();
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * The class of the repository.
     */
    final Class<T> repositoryClass;

    /**
     * The class of the entities of the repository.
     */
    final Class<U> entityClass;

    /**
     * The class of the identifiers of the entities.
     */
    final Class<V> idClass;

    // ---------------------------------------------------------------------------------------------

    /**
     * The instances of the repositories, from which {@link #repositoryInstance()} selects; an
     * injection point may not be of the type variable {@code T}, and a repository no injection
     * point refers to is removed as unused.
     */
    @Inject
    Instance<_Repository<?, ?>> repositoryInstances;
}
