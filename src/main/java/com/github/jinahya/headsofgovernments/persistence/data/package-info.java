/**
 * Jakarta Data repositories for the entities of the heads-of-governments database, which live one
 * package up, in the upstream {@code heads-of-governments-persistence} artifact.
 * <p>
 * Every repository here, but {@code _Repository}, which they extend, is an interface annotated
 * {@link jakarta.data.repository.Repository}; its implementation, a {@code <Name>_} class in this
 * package, is generated at compile time by Hibernate Data Repositories and runs on a
 * {@link org.hibernate.StatelessSession}.
 * <p>
 * The package is {@link NullMarked}: every parameter and return type here is non-null unless marked
 * {@code @Nullable}.
 */
@NullMarked
package com.github.jinahya.headsofgovernments.persistence.data;

import org.jspecify.annotations.NullMarked;
